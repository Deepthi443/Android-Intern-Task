
fun HoldToRecordButton(
  onStart: () -> Unit,
  onStop: (durationMs: Long) -> Unit,
  modifier: Modifier = Modifier
) {
  var isRecording by remember { mutableStateOf(false) }
  var startTime by remember { mutableStateOf(0L) }

  Box(modifier = modifier
    .size(80.dp)
    .pointerInput(Unit) {
      while (true) {
        val event = awaitPointerEventScope { awaitFirstDown() }
        // finger down
        isRecording = true
        startTime = System.currentTimeMillis()
        onStart()
        // wait for up
        awaitPointerEventScope {
          var up = false
          while (!up) {
            val ev = awaitPointerEvent()
            if (ev.changes.any { it.changedToUp() }) up = true
          }
        }
        val duration = System.currentTimeMillis() - startTime
        isRecording = false
        onStop(duration)
      }
    },
    contentAlignment = Alignment.Center
  ) {
    val label = if (isRecording) "Recording..." else "Hold to record"
    Surface(shape = CircleShape, elevation = 4.dp) {
      Box(Modifier.size(80.dp), contentAlignment = Alignment.Center) {
        Text(label, textAlign = TextAlign.Center, modifier = Modifier.padding(8.dp))
      }
    }
  }
}
