#!/bin/bash

ls -1tr gen*.png | xargs -i convert -scale 800% {} {}
convert -delay 10 -loop 0 $(ls -1tr gen*.png) animation.gif
