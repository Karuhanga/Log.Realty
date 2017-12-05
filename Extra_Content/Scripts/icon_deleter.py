##
#@author Karuhanga Lincoln
#Simple utility script for deleting a desired icon from ech of the respective android dpi folders

import os

SLASH= "/"

DIRS= [
'drawable',
'drawable-hdpi',
'drawable-ldrtl-hdpi',
'drawable-ldrtl-mdpi',
'drawable-ldrtl-xhdpi',
'drawable-ldrtl-xxhdpi',
'drawable-ldrtl-xxxhdpi',
'drawable-mdpi',
'drawable-xhdpi',
'drawable-xxhdpi',
'drawable-xxxhdpi']

icon= raw_input("Please input the icon name:\t")
project= raw_input("Please input the destination project path:\t")

DESTINATION_PROJECT_PATH= project+ "/app/src/main/res"

for folder in DIRS:
	files= os.listdir(DESTINATION_PROJECT_PATH+SLASH+folder)
	for file in files:
		print("Finding icon...")
		if icon in file:
			print("Found, deleting...")
			os.remove(DESTINATION_PROJECT_PATH+SLASH+folder+SLASH+file)
		else:
			pass
print("Complete")