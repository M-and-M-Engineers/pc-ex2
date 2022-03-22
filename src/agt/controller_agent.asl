
+!updateRoomStatus : userPolicy(Policy) & detected(Amount) & brightnessLevel(TotalBrightness) & lightBrightnessLevel(LightBrightness) &
    ((Policy == "Always ON" & LightBrightness < 30) |
    (Policy == "Automatic" & Amount >= 1 & TotalBrightness < 30)) <-
    .send(light_agent, achieve, switchOnAndIncreaseBrightness).

+!updateRoomStatus : userPolicy(Policy) & brightnessLevel(TotalBrightness) & detected(Amount) &
    (Policy == "Always OFF" |
    (Policy == "Automatic" &
        (Amount == 0 |
        TotalBrightness > 50)
    )) <-
    .send(light_agent, achieve, switchOffAction).

+!updateRoomStatus.

{ include("$jacamoJar/templates/common-cartago.asl") }
