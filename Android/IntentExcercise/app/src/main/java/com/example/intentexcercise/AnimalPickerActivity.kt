package com.example.intentexcercise

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_animal_picker.*

class AnimalPickerActivity : AppCompatActivity() {

    var animalData : ArrayList<Animal> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_picker)

        animalData.add(Animal("Tiger", R.drawable.tiger))
        animalData.add(Animal("Cat", R.drawable.cat))
        animalData.add(Animal("Dog", R.drawable.dog))
        animalData.add(Animal("Elephant", R.drawable.elephant))
        animalData.add(Animal("Bird", R.drawable.bird))
        animalData.add(Animal("Mouth", R.drawable.mouth))
        animalData.add(Animal("Squirrel", R.drawable.squirrel))

        gv_animalGellary.adapter = AnimalAdapter(R.layout.gridview_animal_detail, animalData, this.applicationContext)

        gv_animalGellary.setOnItemClickListener { parent, view, position, id ->
           // Toast.makeText(this, "Selected animal is ${animalData.get(position).name}", Toast.LENGTH_SHORT).show()
            var resultIntent : Intent = Intent()
            resultIntent.putExtra("picId", animalData.get(position).pictureID)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}
