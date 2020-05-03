package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTestRepository
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

class TasksViewModelTest {

    // Subject under test
    private lateinit var tasksViewModel: TasksViewModel

    // Use a fake repository to be injected into the view
    private lateinit var tasksRepository: FakeTestRepository

    @Before
    fun setupViewModel() {
        // We initialize the tasks to 3, with one active
        tasksRepository = FakeTestRepository()
        val task1 = Task("Title1", "Des 1")
        val task2 = Task("Title2", "Des 2", true)
        val task3 = Task("Title3", "Des 3", true)
        tasksRepository.addTasks(task1, task2, task3)

        tasksViewModel = TasksViewModel(tasksRepository)
    }

    @get:Rule
    var instantExcutorRule = InstantTaskExecutorRule()


   @Test
    fun addNewTask_setNewTaskEvent() {
       // When adding a new task
       tasksViewModel.addNewTask()

       // Then the new task event is triggered
       val value = tasksViewModel.newTaskEvent.getOrAwaitValue()

       assertThat(value.getContentIfNotHandled(), not(nullValue()))
    }

    @Test
    fun setFilterAllTasks_taskAddViewVisible() {
        // When set filter type ALL_TASK
        tasksViewModel.setFiltering(TasksFilterType.ALL_TASKS)

        // Then the "Add task" action is visible
        assertThat(tasksViewModel.tasksAddViewVisible.getOrAwaitValue(), `is`(true))
    }
}