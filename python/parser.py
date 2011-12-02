#!/usr/bin/env python
#
# a parser for this strange text file
#
# available under the free beer license
#
# USAGE ./parser.py path_to_your_templates_file
# 
# if you have installed pyplot the first 10 templates are plotted

PLOT = True
try: 
    import matplotlib.pyplot as plt
except:
    PLOT = False

import sys

class Template():
   
    __width=0
    __height=0
    __Xres=0
    __Yres=0
    __minutiae=None
    
    def __init__(self):
        self.__minutiae = []

    def setWidth(self, width):
        self.__width = width

    def setHeight(self, height):
        self.__height = height 

    def setXRes(self, resolution):
        self.__Xres = resolution

    def setYRes(self, resolution):
        self.__Yres = resolution

    def addMinutia(self, index, x, y, angle, quality, minType):
        self.__minutiae.append((index,x,y,angle,quality,minType))

    def getMinutiae(self):
        return self.__minutiae
    
    def getHeight(self):
        return self.__height

    def getWidth(self):
        return self.__width

# helper func
def getValue(line):
    try:
        return int(line.split(':')[1])
    except:
        print line

# creates a list of Templates
def parseFile(fileName):
    f = file(fileName,'r')
    t=None
    fingers = []
    for l in f.readlines():
        if 'RecordHeader of Isotemplate' in l:
            t = Template()
            fingers.append(t)
        elif 'Image Width:' in l:
            t.setWidth(getValue(l))
        elif 'Image Height:' in l:
            t.setHeight(getValue(l))
        elif 'X Resolution:' in l:
            t.setXRes(getValue(l))
        elif 'Y Resolution:' in l:
            t.setYRes(getValue(l))
        elif 'MinutiaIndex:' in l:
            index = getValue(l)
            x = None
            y = None
            angle = None
            quality = None
            minType = None 
        elif 'xCoord:' in l:
            x = getValue(l)
        elif 'minType:' in l:
            type = getValue(l) 
        elif 'yCoord:' in l:
            y = getValue(l)
        elif 'minAngle:' in l:
            angle = getValue(l)
        elif 'minQuality:' in l:
            quality = getValue(l)
            t.addMinutia(index,x,y,angle,quality,minType)
    f.close()
    return fingers

def plot(finger):
    x_coords = [x[1] for x in finger.getMinutiae()]
    y_coords = [y[2] for y in finger.getMinutiae()]
    plt.plot(x_coords, y_coords, 'ro')
    size = max(finger.getWidth(), finger.getHeight())
    plt.axis([0,size,0,size])
    plt.show()   

if __name__ == '__main__':
    if len(sys.argv) < 2:
        print """
USAGE: ./parser.py PATH_TO_YOUR_FILE

"""
        sys.exit(1)

    fingers = parseFile(sys.argv[1])

    if(PLOT):
        # debugging... show 10 fingers
        for f in fingers[:10]:
            plot(f)
