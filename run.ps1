$javaCmd = Get-Command java
$javaSource = $javaCmd.Source
& $javaSource -jar 'D:\plusplus\checkcc\build\libs\checkcc-0.0.1.jar'