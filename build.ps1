.\gradlew build
cp build\libs\checkcc-0.0.1.jar .

rm checkcc.zip

$compress = @{
  Path = "checkcc-0.0.1.jar", "install.ps1", "Checkcc.exe", "Checkcc.xml", "conf.json"
  CompressionLevel = "Fastest"
  DestinationPath = "checkcc"
}
Compress-Archive @compress
