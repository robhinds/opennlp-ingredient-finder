package io.github.robhinds.ner

import java.io.{FileInputStream, IOException}

import opennlp.tools.namefind.{NameFinderME, TokenNameFinderModel}
import org.scalatest.{FunSpec, Matchers}

class NerModelTrainerSpec extends FunSpec with Matchers {

  describe("training a model"){
    it ("should create a trained model file") {
      NerModelTrainer.trainModel()
    }
  }

  val recipe = "Make the shortbread - cream the butter and sugar, mix in the flour, and then press into the bottom of a lined baking tray\n\nFor the caramel, gently melt all ingredients except rosemary in a pan, once melted add the rosemary and stir through, heat for two to three minutes\n\nLeave the caramel to stand for another 10 minutes or so, whilst the shortbread cools\n\nPoor the caramel through a sieve to remove the rosemary pieces, pour caramel onto the cooled shortbread\n\nPlace the caramel topped shortbread in the fridge to cool for another 20 minutes\n\nMelt the chocolate in the microwave on a low power settings (will take a couple of minutes), once it is smooth, pour on top of the caramel shortbread and put back in the fridge to set."

  describe("running trained model") {
    it("should extract ingredients") {
      val modelIn = new FileInputStream("src/main/resources/en-ingredients-finder.bin")

      try {
        val sampleRecipe = recipe.split("[^a-zA-Z]")
        val model = new TokenNameFinderModel(modelIn)
        val nameFinder = new NameFinderME(model)
        val matches = nameFinder.find(sampleRecipe)
        matches.foreach { m =>
          sampleRecipe.slice(m.getStart, m.getEnd).foreach(println(_))
        }
      }
      finally {
        if (modelIn != null) {
          try {
            modelIn.close()
          }
        }
      }
    }
  }

}
