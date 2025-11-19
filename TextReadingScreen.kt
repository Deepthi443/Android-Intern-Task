@Composable
fun TextReadingScreen(onSubmit: (SampleTask) -> Unit) {
  val context = LocalContext.current
  val recorder = remember { RecordingHelper(context) }
  var audioFile by remember { mutableStateOf<File?>(null) }
  var durationSec by remember { mutableStateOf(0) }
  var error by remember { mutableStateOf<String?>(null) }
  var checked1 by remember { mutableStateOf(false) }
  var checked2 by remember { mutableStateOf(false) }
  var checked3 by remember { mutableStateOf(false) }

  val sampleText = "Mega long lasting fragrance..."
  Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
    Text(sampleText)
    Spacer(Modifier.height(12.dp))
    HoldToRecordButton(
      onStart = { 
        // runtime permission check required
        recorder.startRecording()
      },
      onStop = { durationMs ->
        val sec = (durationMs / 1000).toInt()
        durationSec = sec
        if (sec < 10) {
          error = "Recording too short (min 10 s)."
          // delete file if created
          audioFile = recorder.stopRecording()
          audioFile?.delete()
          audioFile = null
        } else if (sec > 20) {
          error = "Recording too long (max 20 s)."
          audioFile = recorder.stopRecording()
          audioFile?.delete()
          audioFile = null
        } else {
          error = null
          audioFile = recorder.stopRecording()
        }
      }
    )
    if (error != null) { Text(error!!, color = Color.Red) }
    Spacer(Modifier.height(12.dp))
    // playback: simple MediaPlayer
    if (audioFile != null) {
      Button(onClick = { /* play audioFile via MediaPlayer */ }) {
        Text("Play")
      }
    }
    Spacer(Modifier.height(12.dp))
    Row { Checkbox(checked1, onCheckedChange = { checked1 = it }); Text("No background noise") }
    Row { Checkbox(checked2, onCheckedChange = { checked2 = it }); Text("No mistakes while reading") }
    Row { Checkbox(checked3, onCheckedChange = { checked3 = it }); Text("Beech me koi galti nahi hai") }
    Spacer(Modifier.height(12.dp))
    Row {
      OutlinedButton(onClick = { /* record again: reset state to allow re-record */ audioFile?.delete(); audioFile = null; durationSec = 0 }) { Text("Record again") }
      Spacer(Modifier.width(8.dp))
      Button(onClick = {
        val task = SampleTask(
          id = java.util.UUID.randomUUID().toString(),
          task_type = "text_reading",
          text = sampleText,
          audio_path = audioFile?.absolutePath,
          duration_sec = durationSec,
          timestamp = java.time.OffsetDateTime.now().toString()
        )
        onSubmit(task)
      }, enabled = checked1 && checked2 && checked3 && audioFile != null) {
        Text("Submit")
      }
    }
  }
}
