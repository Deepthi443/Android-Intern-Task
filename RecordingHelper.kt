class RecordingHelper(private val context: Context) {

  private var recorder: MediaRecorder? = null
  private var outputFile: File? = null

  fun startRecording(fileName: String = "audio_${System.currentTimeMillis()}.mp4") {
    val dir = context.getExternalFilesDir("recordings") ?: context.filesDir
    outputFile = File(dir, fileName)
    recorder = MediaRecorder().apply {
      setAudioSource(MediaRecorder.AudioSource.MIC)
      setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
      setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
      setAudioSamplingRate(44100)
      setAudioEncodingBitRate(96000)
      setOutputFile(outputFile!!.absolutePath)
      prepare()
      start()
    }
  }

  fun stopRecording(): File? {
    return try {
      recorder?.apply {
        stop()
        reset()
        release()
      }
      recorder = null
      outputFile
    } catch (e: Exception) {
      recorder = null
      null
    }
  }
}
