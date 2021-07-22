package example.com.database

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var dbrw: SQLiteDatabase

    private var items: ArrayList<String> = ArrayList()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //宣告Adapter並連結ListView
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        listView.adapter = adapter
        //取得資料庫實體
        dbrw = MySQLiteOpenHelper(this).writableDatabase

        btn_query.setOnClickListener {
            val c = dbrw.rawQuery(if(ed_id.length()<1) "SELECT * FROM Phone"
            else "SELECT * FROM Phone WHERE id LIKE '${ed_id.text}'",null)
            //從第一筆開始輸出
            c.moveToFirst()
            //清空舊資料
            items.clear()
            showToast("共有${c.count}筆資料")
            for (i in 0 until c.count) {
                items.add("編號:${c.getInt(0)}\n姓名:${c.getString(1)}\n手機:${c.getInt(2)}\n地址:${c.getString(3)}\n備註:${c.getString(4)}")
                //移動到下一筆
                c.moveToNext()
            }
            //更新列表資料
            adapter.notifyDataSetChanged()
            //關閉Cursor
            c.close()
        }

        btn_insert.setOnClickListener {
            //編號 姓名 手機是否為空
            if (ed_id.length()<1 || ed_name.length()<1 || ed_phone.length()<1)
                showToast("請輸入必要欄位")
            else
                try{
                    //新增一筆資料進入Phone資料表
                    dbrw.execSQL("INSERT INTO Phone(id, name, phone, address, note) VALUES(?,?,?,?,?)",
                        arrayOf<Any?>(ed_id.text.toString(), ed_name.text.toString(), ed_phone.text.toString(), ed_address.text.toString(),ed_note.text.toString()))
                    showToast("新增編號${ed_id.text}  姓名${ed_name.text}  手機${ed_phone.text}")
                    cleanEditText()
                }catch (e: Exception){
                    showToast("新增失敗:$e")
                }
        }

        btn_update.setOnClickListener {
            //判斷是否有填入編號姓名手機
            if (ed_id.length()<1 || ed_name.length()<1 || ed_phone.length()<1)
                showToast("請輸入必要欄位")
            else
                try{
                    //更新id欄位為輸入字串（ed_id）的資料的其他欄位數值
                    dbrw.execSQL("UPDATE Phone SET name = ${ed_name.text} WHERE id = '${ed_id.text}'")
                    dbrw.execSQL("UPDATE Phone SET phone = ${ed_phone.text} WHERE id LIKE '${ed_id.text}'")
                    dbrw.execSQL("UPDATE Phone SET address = ${ed_address.text} WHERE id LIKE '${ed_id.text}'")
                    dbrw.execSQL("UPDATE Phone SET note = ${ed_note.text} WHERE id LIKE '${ed_id.text}'")
                    showToast("更新編號${ed_id.text}  姓名${ed_name.text}  手機${ed_phone.text}")
                    cleanEditText()
                }catch (e: Exception){
                    showToast("更新失敗:$e")
                }
        }

        btn_delete.setOnClickListener {
            //判斷是否沒有填入編號
            if (ed_id.length()<1)
                showToast("請輸入編號")
            else
                try{
                    //從Phone資料表刪除id欄位為輸入字串（ed_id）的資料
                    dbrw.execSQL("DELETE FROM Phone WHERE id LIKE '${ed_id.text}'")
                    showToast("刪除編號${ed_id.text}")
                    cleanEditText()
                }catch (e: Exception){
                    showToast("刪除失敗:$e")
                }
        }
    }

    private fun showToast(text: String) =
        Toast.makeText(this,text, Toast.LENGTH_LONG).show()
    //清空輸入欄
    private fun cleanEditText(){
        ed_id.setText("")
        ed_name.setText("")
        ed_phone.setText("")
        ed_address.setText("")
        ed_note.setText("")
    }

    override fun onDestroy() {
        super.onDestroy()
        //關閉資料庫
        dbrw.close()
    }
}