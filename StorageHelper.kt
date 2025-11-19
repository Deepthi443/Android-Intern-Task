class StorageHelper(private val context: Context) {
  private val json = Json { prettyPrint = true }

  private fun tasksFile(): File {
    return File(context.filesDir, "tasks.json")
  }

  fun loadTasks(): List<SampleTask> {
    val file = tasksFile()
    if (!file.exists()) return emptyList()
    return try {
      val text = file.readText()
      json.decodeFromString(ListSerializer(SampleTask.serializer()), text)
    } catch (e: Exception) {
      emptyList()
    }
  }

  fun saveTask(task: SampleTask) {
    val cur = loadTasks().toMutableList()
    cur.add(0, task)
    tasksFile().writeText(json.encodeToString(ListSerializer(SampleTask.serializer()), cur))
  }
}
