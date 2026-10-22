package com.example.util

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.data.model.OMRResult
import com.example.data.model.QuestionPaper
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfExporter {

  fun generateQuestionPaperPdf(
    context: Context,
    paper: QuestionPaper,
    isMalayalam: Boolean = false,
  ): File? {
    val document = PdfDocument()
    val pageWidth = 595 // A4 standard width at 72dpi
    val pageHeight = 842 // A4 standard height at 72dpi
    var pageNumber = 1

    try {
      var pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
      var page = document.startPage(pageInfo)
      var canvas = page.canvas

      val textPaint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        textSize = 10f
      }

      val headerPaint = Paint().apply {
        isAntiAlias = true
        color = Color.rgb(0, 106, 103)
        textSize = 14f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      }

      val subHeaderPaint = Paint().apply {
        isAntiAlias = true
        color = Color.rgb(74, 99, 97)
        textSize = 10f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      }

      val boldPaint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        textSize = 10.5f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      }

      val borderPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 1f
        color = Color.rgb(200, 210, 210)
      }

      var y = 40f
      val leftMargin = 40f
      val rightMargin = pageWidth - 40f
      val contentWidth = rightMargin - leftMargin

      fun drawHeader() {
        canvas.drawRect(leftMargin, y, rightMargin, y + 60f, borderPaint)
        val title = if (isMalayalam) paper.titleMl else paper.titleEn
        canvas.drawText(title, leftMargin + 10f, y + 22f, headerPaint)
        canvas.drawText(
          "Exam: ${paper.examPreference.displayName}  |  Level: ${paper.educationLevel.label}  |  Year: ${paper.year}",
          leftMargin + 10f,
          y + 38f,
          subHeaderPaint
        )
        canvas.drawText(
          "Time: ${paper.durationMinutes} Mins  |  Marks: ${paper.totalMarks.toInt()}  |  Negative: -${paper.negativeMarkingPerWrong}",
          leftMargin + 10f,
          y + 52f,
          textPaint
        )
        y += 75f
      }

      fun startNewPage() {
        // Draw page footer
        val footerText = "Page $pageNumber  •  Exam&MindCare Hub"
        canvas.drawText(footerText, pageWidth / 2f - 60f, pageHeight - 20f, textPaint)
        document.finishPage(page)
        pageNumber++
        pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
        page = document.startPage(pageInfo)
        canvas = page.canvas
        y = 40f
      }

      drawHeader()

      // Questions loop
      paper.questions.forEachIndexed { index, q ->
        if (y > pageHeight - 120f) {
          startNewPage()
        }

        val qNumber = "Q${index + 1}. "
        val questionText = if (isMalayalam && q.questionMl.isNotBlank()) q.questionMl else q.questionEn
        val fullQuestion = qNumber + questionText

        // Wrap question text
        val lines = splitTextIntoLines(fullQuestion, boldPaint, contentWidth)
        for (line in lines) {
          if (y > pageHeight - 60f) {
            startNewPage()
          }
          canvas.drawText(line, leftMargin, y, boldPaint)
          y += 14f
        }
        y += 4f

        // Options
        val options = if (isMalayalam && q.optionsMl.isNotEmpty()) q.optionsMl else q.optionsEn
        val optionPrefixes = listOf("(A) ", "(B) ", "(C) ", "(D) ")

        options.forEachIndexed { optIdx, optText ->
          val optLabel = optionPrefixes.getOrElse(optIdx) { "" } + optText
          val optLines = splitTextIntoLines(optLabel, textPaint, contentWidth - 15f)
          for (line in optLines) {
            if (y > pageHeight - 40f) {
              startNewPage()
            }
            canvas.drawText(line, leftMargin + 15f, y, textPaint)
            y += 13f
          }
        }

        y += 12f
        canvas.drawLine(leftMargin, y, rightMargin, y, borderPaint)
        y += 14f
      }

      // Draw Answer Key section at end
      if (y > pageHeight - 140f) {
        startNewPage()
      }
      y += 10f
      canvas.drawText("ANSWER KEY & REFERENCES", leftMargin, y, headerPaint)
      y += 18f

      val ansRow = StringBuilder()
      paper.questions.forEachIndexed { idx, q ->
        val ansLetter = when (q.correctIndex) {
          0 -> "A"
          1 -> "B"
          2 -> "C"
          3 -> "D"
          else -> "?"
        }
        ansRow.append("${idx + 1}:$ansLetter   ")
        if ((idx + 1) % 10 == 0) {
          canvas.drawText(ansRow.toString(), leftMargin, y, boldPaint)
          y += 16f
          ansRow.setLength(0)
        }
      }
      if (ansRow.isNotEmpty()) {
        canvas.drawText(ansRow.toString(), leftMargin, y, boldPaint)
        y += 16f
      }

      // Final footer
      canvas.drawText("Page $pageNumber  •  Exam&MindCare Hub", pageWidth / 2f - 60f, pageHeight - 20f, textPaint)
      document.finishPage(page)

      val outputDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) ?: context.filesDir
      val fileName = "ExamMindCare_${paper.id}_${System.currentTimeMillis()}.pdf"
      val file = File(outputDir, fileName)
      val fos = FileOutputStream(file)
      document.writeTo(fos)
      fos.close()
      document.close()
      return file
    } catch (e: Exception) {
      e.printStackTrace()
      document.close()
      return null
    }
  }

  fun generateOMRScorecardPdf(
    context: Context,
    result: OMRResult,
    candidateName: String,
    phoneNumber: String,
    educationLevel: String,
  ): File? {
    val document = PdfDocument()
    val pageWidth = 595
    val pageHeight = 842

    try {
      val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
      val page = document.startPage(pageInfo)
      val canvas = page.canvas

      val titlePaint = Paint().apply {
        isAntiAlias = true
        color = Color.rgb(0, 106, 103)
        textSize = 18f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      }

      val sectionPaint = Paint().apply {
        isAntiAlias = true
        color = Color.rgb(74, 99, 97)
        textSize = 12f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      }

      val textPaint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        textSize = 10f
      }

      val boldTextPaint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        textSize = 11f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      }

      val scorePaint = Paint().apply {
        isAntiAlias = true
        color = Color.rgb(0, 106, 103)
        textSize = 28f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      }

      val borderPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 1f
        color = Color.rgb(180, 200, 200)
      }

      val bgBoxPaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.rgb(240, 247, 246)
      }

      val left = 40f
      val right = pageWidth - 40f
      var y = 50f

      // Header
      canvas.drawText("EXAM & MINDCARE HUB ASSESSMENT SCORECARD", left, y, titlePaint)
      y += 18f
      val dateStr = SimpleDateFormat("dd MMMM yyyy, hh:mm a", Locale.getDefault()).format(Date(result.timestamp))
      canvas.drawText("Verified Assessment Report  •  $dateStr", left, y, textPaint)
      y += 20f

      // Candidate Profile Box
      canvas.drawRect(left, y, right, y + 70f, bgBoxPaint)
      canvas.drawRect(left, y, right, y + 70f, borderPaint)

      canvas.drawText("CANDIDATE DETAILS", left + 15f, y + 18f, sectionPaint)
      canvas.drawText("Name: $candidateName", left + 15f, y + 36f, boldTextPaint)
      canvas.drawText("Phone: $phoneNumber", left + 15f, y + 54f, textPaint)
      canvas.drawText("Target: ${result.examPreference.displayName}", left + 260f, y + 36f, boldTextPaint)
      canvas.drawText("Education: $educationLevel", left + 260f, y + 54f, textPaint)
      y += 85f

      // Score Summary Banner
      canvas.drawRect(left, y, right, y + 90f, bgBoxPaint)
      canvas.drawRect(left, y, right, y + 90f, borderPaint)

      canvas.drawText("TOTAL SCORE OBTAINED", left + 15f, y + 25f, sectionPaint)
      canvas.drawText("${String.format(Locale.US, "%.2f", result.score)} / ${result.totalMarks.toInt()}", left + 15f, y + 65f, scorePaint)

      canvas.drawText("Accuracy: ${String.format(Locale.US, "%.1f", result.accuracyPercent)}%", left + 240f, y + 35f, boldTextPaint)
      val timeMinutes = result.timeTakenSeconds / 60
      val timeSeconds = result.timeTakenSeconds % 60
      canvas.drawText("Time Taken: ${timeMinutes}m ${timeSeconds}s", left + 240f, y + 55f, textPaint)
      canvas.drawText("Paper: ${result.paperTitle}", left + 240f, y + 75f, textPaint)
      y += 110f

      // Breakdown Table
      canvas.drawText("PERFORMANCE METRICS BREAKDOWN", left, y, sectionPaint)
      y += 15f

      val colW = (right - left) / 4f
      val metrics = listOf(
        Pair("Attempted", "${result.attempted}"),
        Pair("Correct", "${result.correct}"),
        Pair("Wrong (-0.33)", "${result.wrong}"),
        Pair("Omitted", "${result.omitted}")
      )

      metrics.forEachIndexed { i, pair ->
        val boxX = left + i * colW
        canvas.drawRect(boxX, y, boxX + colW - 6f, y + 45f, bgBoxPaint)
        canvas.drawRect(boxX, y, boxX + colW - 6f, y + 45f, borderPaint)
        canvas.drawText(pair.first, boxX + 8f, y + 16f, textPaint)
        canvas.drawText(pair.second, boxX + 8f, y + 36f, boldTextPaint)
      }
      y += 65f

      // Mind Wellness Recommendation
      canvas.drawText("MIND WELLNESS & STUDY ADVICE", left, y, sectionPaint)
      y += 18f
      val advice = if (result.accuracyPercent >= 75.0) {
        "Excellent mastery! Maintain this momentum. Practice 5-minute Box Breathing to sustain steady concentration."
      } else if (result.accuracyPercent >= 50.0) {
        "Good effort with strong potential. Focus on eliminating negative marks through mindful question selection. Use 4-7-8 breathing to stay relaxed."
      } else {
        "Keep going! Review fundamental concepts in the syllabus. Take 10-minute eye relaxation breaks and practice subject-wise sets."
      }
      val adviceLines = splitTextIntoLines(advice, textPaint, right - left)
      for (line in adviceLines) {
        canvas.drawText(line, left, y, textPaint)
        y += 14f
      }

      // Footer
      canvas.drawText("Generated by Exam&MindCare Hub  •  Higher Exam Prep & Mindfulness", left, pageHeight - 30f, textPaint)

      document.finishPage(page)

      val outputDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) ?: context.filesDir
      val fileName = "ExamMindCare_Scorecard_${System.currentTimeMillis()}.pdf"
      val file = File(outputDir, fileName)
      val fos = FileOutputStream(file)
      document.writeTo(fos)
      fos.close()
      document.close()
      return file
    } catch (e: Exception) {
      e.printStackTrace()
      document.close()
      return null
    }
  }

  fun openOrSharePdf(context: Context, file: File, title: String) {
    try {
      val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
      )
      val intent = Intent(Intent.ACTION_SEND).apply {
        type = "application/pdf"
        putExtra(Intent.EXTRA_STREAM, uri)
        putExtra(Intent.EXTRA_SUBJECT, title)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
      }
      val chooser = Intent.createChooser(intent, "Open or Share $title")
      chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      context.startActivity(chooser)
    } catch (e: Exception) {
      Toast.makeText(context, "Saved to ${file.name}", Toast.LENGTH_LONG).show()
    }
  }

  private fun splitTextIntoLines(text: String, paint: Paint, maxWidth: Float): List<String> {
    val lines = mutableListOf<String>()
    val words = text.split(" ")
    var currentLine = StringBuilder()

    for (word in words) {
      val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
      val width = paint.measureText(testLine)
      if (width <= maxWidth) {
        currentLine = StringBuilder(testLine)
      } else {
        if (currentLine.isNotEmpty()) {
          lines.add(currentLine.toString())
        }
        currentLine = StringBuilder(word)
      }
    }
    if (currentLine.isNotEmpty()) {
      lines.add(currentLine.toString())
    }
    return lines
  }
}
