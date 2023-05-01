# $javaCmd = Get-Command java
# $javaSource = $javaCmd.Source
# & $javaSource --version
# $params = @{
#   Name = "TestRunCheckcc"
#   BinaryPathName = "powershell D:\plusplus\checkcc\run.ps1"
#   DisplayName = "TestRunCheckcc"
#   StartupType = "Manual"
#   Description = "This is a test service."
# }
# New-Service @params

.\Checkcc.exe uninstall
Start-Sleep -Seconds 5
.\Checkcc.exe install
