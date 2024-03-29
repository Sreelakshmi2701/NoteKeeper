package com.sree.notekeeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_note_list.*

class MainActivity : AppCompatActivity() {
private var notePosition= POSITION_NOT_SET
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //val dm = DataManager()
        val adapterCourses = ArrayAdapter<CourseInfo>(this,android.R.layout.simple_spinner_item,DataManager.courses.values.toList()
        )
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerCourses.adapter=adapterCourses
        notePosition=savedInstanceState?.getInt(EXTRA_NOTE_LIST_POSITION, POSITION_NOT_SET) ?:
            intent.getIntExtra(EXTRA_NOTE_LIST_POSITION, POSITION_NOT_SET)

        if(notePosition!= POSITION_NOT_SET)
            displayNote()
        else{
            DataManager.notes.add(NoteInfo())
            notePosition= DataManager.notes.lastIndex
        }
    }

    private fun displayNote() {
        val note =DataManager.notes[notePosition]
        textNoteTitle.setText(note.title)
        textNoteText.setText(note.text)

        val coursePosition= DataManager.courses.keys.indexOf(note.course?.courseId)
        spinnerCourses.setSelection(coursePosition)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_settings->true
            R.id.action_next->{
                moveNext()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun moveNext() {
        ++notePosition
        displayNote()
        invalidateOptionsMenu()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if(notePosition >= DataManager.notes.lastIndex){
            val menuItem =menu?.findItem(R.id.action_next)
            if(menuItem!=null){
                menuItem.icon= getDrawable(R.drawable.ic_block_black_24dp)
                menuItem.isEnabled= false
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onPause() {
        super.onPause()
        saveNote()
    }

    private fun saveNote() {
        val note = DataManager.notes[notePosition]
        note.title=  textNoteTitle.text.toString()
        note.text= textNoteText.text.toString()
        note.course= spinnerCourses.selectedItem as CourseInfo

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(EXTRA_NOTE_LIST_POSITION,notePosition)
    }


}
