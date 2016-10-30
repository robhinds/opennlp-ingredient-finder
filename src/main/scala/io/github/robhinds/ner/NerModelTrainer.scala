package io.github.robhinds.ner

import java.io.{BufferedOutputStream, FileInputStream, FileOutputStream}
import java.nio.charset.Charset

import opennlp.tools.ml.maxent.quasinewton.QNTrainer
import opennlp.tools.ml.perceptron.PerceptronTrainer
import opennlp.tools.namefind.{NameFinderME, NameSampleDataStream, TokenNameFinderFactory, TokenNameFinderModel}
import opennlp.tools.util.{ObjectStream, PlainTextByLineStream, TrainingParameters}

object NerModelTrainer {
  var model: TokenNameFinderModel = _
  var modelOut: BufferedOutputStream = _

  def trainModel() = {

    val charset = Charset.forName("UTF-8")
    val lineStream: ObjectStream[String] = new PlainTextByLineStream(new FileInputStream(s"src/main/resources/trainingdata.txt"), charset)
    val sampleStream = new NameSampleDataStream(lineStream)

    try {
      val params = TrainingParameters.defaultParams()
      params.put(TrainingParameters.ITERATIONS_PARAM, "200")
      params.put(TrainingParameters.ALGORITHM_PARAM, QNTrainer.MAXENT_QN_VALUE)

      model = NameFinderME.train("en", "food", sampleStream, params, new TokenNameFinderFactory())
    }
    finally {
      sampleStream.close()
    }

    try {
      modelOut = new BufferedOutputStream(new FileOutputStream(s"src/main/resources/en-ingredients-finder.bin"))
      model.serialize(modelOut)
    } finally {
      if (modelOut != null)
        modelOut.close()
    }
  }
}