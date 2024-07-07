rootProject.name = "FL-Refs"

sourceControl {
  gitRepository(java.net.URI("https://github.com/LucasAlfare/FL-Base")) {
    producesModule("com.lucasalfare.flbase:FL-Base")
  }

  gitRepository(java.net.URI("https://github.com/LucasAlfare/kGasC")) {
    producesModule("com.lucasalfare.kgasc:kGasC")
  }
}