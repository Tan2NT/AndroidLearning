package com.example.RSS

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringReader
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var rss = ReadRSS(this)
        rss.execute("https://vnexpress.net/rss/so-hoa.rss")
    }

    private class ReadRSS(var activity: MainActivity) : AsyncTask<String, Void, String>(){

        override fun doInBackground(vararg params: String?): String {
            var content : StringBuilder = java.lang.StringBuilder()
            try{
                var url : URL = URL(params[0])
                var inputStreamReader : InputStreamReader = InputStreamReader(url.openConnection().getInputStream())
                var bufferReader : BufferedReader = BufferedReader(inputStreamReader)
                var line : String = bufferReader.readLine()
                while(line != null){
                    if(line == null)
                        break

                    content.append(line)

                    line = bufferReader.readLine()
                }

                bufferReader.close()
            }
            catch(e: Exception){

            }

            return content.toString()
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onPostExecute(result: String?) {

            val xmlDoc : Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(InputSource(StringReader(result)))
            xmlDoc.documentElement.normalize()

            val nodeList : NodeList = xmlDoc.getElementsByTagName("item");
            Toast.makeText(activity, "item size is ${nodeList.length}", Toast.LENGTH_LONG).show()

            var titles : String = ""

            var magazineTitles : ArrayList<String> = ArrayList()
            var magazineLinks : ArrayList<String> = ArrayList()

            for(i in 0..nodeList.length - 1){
                var element : Element = nodeList.item(i) as Element

                val mMap = mutableMapOf<String, String>()

                for(j in 0..element.attributes.length - 1)
                {
                    mMap.put(element.attributes.item(j).nodeName, element.attributes.item(j).nodeValue)
                }

                var title : String = element.getElementsByTagName("title").item(0).textContent
                magazineTitles.add(title)

                var link : String = element.getElementsByTagName("link").item(0).textContent
                magazineLinks.add(link)
            }

            activity.lvMagazineList.adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, magazineTitles)

            activity.lvMagazineList.setOnItemClickListener { parent, view, position, id ->

                    var magazineIntent : Intent = Intent(activity, MagazineDetail::class.java)
                    magazineIntent.putExtra("link", magazineLinks.get(position))
                    activity.startActivity(magazineIntent)
            }

            super.onPostExecute(result)
        }

    }
}
