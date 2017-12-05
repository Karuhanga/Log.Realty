##
#@author Karuhanga Lincoln
#Simple utility script for copying a desired icon to each of the respective desired android dpi folders

import os
import shutil

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

ICON_PATH= "//home/karuhanga/Documents/Design/material-design-icons-android/joints/"
DESTINATION_PROJECT_PATH= project+ "/app/src/main/res"

def create_directories():
	print("Verifying destinations...")
	for folder in DIRS:
		if not os.path.exists(DESTINATION_PROJECT_PATH+SLASH+folder):
			os.mkdir(DESTINATION_PROJECT_PATH+SLASH+folder)

create_directories()

for folder in DIRS[:1]:
	files= os.listdir(ICON_PATH+SLASH+folder)
	for file in files:
		print("Finding icon...")
		if icon in file:
			print("Found, copying...")
			shutil.copy(ICON_PATH+SLASH+folder+SLASH+file, DESTINATION_PROJECT_PATH+SLASH+folder+SLASH+file)
		else:
			pass
print("Complete")