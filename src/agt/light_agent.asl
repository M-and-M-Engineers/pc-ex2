
+!switchOffAction : status(Status) & Status == "ON" <-
    .println("Switching lamp off");
    switchOff.

+!switchOffAction.

+!increaseBrightnessAction : lightBrightnessLevel(Level) <-
    .println("Increasing lamp brightness to ", Level + 1);
    increaseBrightness.


+!switchOnAndIncreaseBrightness : status(Status) & Status == "OFF" <-
    .println("Switching lamp on");
    switchOn;
    !increaseBrightnessAction.

+!switchOnAndIncreaseBrightness : status(Status) & Status == "ON" <-
    !increaseBrightnessAction.

{ include("$jacamoJar/templates/common-cartago.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
