# MultiBomberman project
---------------------
[![travis build](https://travis-ci.org/alexis-puska/MultiBomberman.svg?branch=master)](https://travis-ci.org/alexis-puska/MultiBomberman) 
[![travis build](https://sonarcloud.io/api/project_badges/measure?project=MultiBomberman&metric=alert_status)](https://sonarcloud.io/dashboard?id=MultiBomberman) [![travis build](https://sonarcloud.io/api/project_badges/measure?project=MultiBomberman&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=MultiBomberman) [![travis build](https://sonarcloud.io/api/project_badges/measure?project=MultiBomberman&metric=security_rating)](https://sonarcloud.io/dashboard?id=MultiBomberman) [![travis build](https://sonarcloud.io/api/project_badges/measure?project=MultiBomberman&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=MultiBomberman)
[![travis build](https://sonarcloud.io/api/project_badges/measure?project=MultiBomberman&metric=ncloc)](https://sonarcloud.io/dashboard?id=MultiBomberman) [![travis build](https://sonarcloud.io/api/project_badges/measure?project=MultiBomberman&metric=bugs)](https://sonarcloud.io/dashboard?id=MultiBomberman) [![travis build](https://sonarcloud.io/api/project_badges/measure?project=MultiBomberman&metric=code_smells)](https://sonarcloud.io/dashboard?id=MultiBomberman) [![travis build](https://sonarcloud.io/api/project_badges/measure?project=MultiBomberman&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=MultiBomberman) [![travis build](https://sonarcloud.io/api/project_badges/measure?project=MultiBomberman&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=MultiBomberman)
---------------------
> MultiBomberman, game adaptation bomberman, in multi player over multi computer, up to 16 player with LibGdx java game library.
---------------------




**Compilation RASPBERRY PI (RETROPIE)**

*connection SSH :*
```sh
reset ssh token : ssh-keygen -R "ip";
reset smb password : smbpasswd -a pi
```

*Installation of the java jdk*
```sh
sudo apt-get install oracle-java8-jdk
```

*Installation on retropie :*

add this xml fragment to es_systems.cfg file in /etc/emulationstation/es_systems.cfg
```
  <system>
    <name>MultiBomberman</name>
    <fullname>MultiBomberman</fullname>
    <plateform>MultiBomberman</plateform>
    <path>/home/pi/RetroPie/roms/MultiBomberman</path>
    <command>sudo %ROM%</command>
    <extension>.sh</extension>
    <theme>MultiBomberman</theme>
  </system>
```


in a folder clone the project : 
```
cd /home/pi/RetroPie/roms
git clone https://github.com/alexis-puska/MultiBomberman.git
cd MultiBomberman
chmod +x Update.sh
chmod +x Generate\ pi.sh
chmod +x Bomberman.sh
./Generate\ pi.sh
cd emulationStationTheme
sudo cp -avr MultiBomberman /etc/emulationstation/themes/carbon
sudo reboot
```

After this step "In The Well" is installed, compile, and integrated in emulation station on retropie distribution. If an update of code is made, you can just launch the "Update" line in emulation station, "Generate pi" and launch the game with "Enter In The Well" :) Enjoy !





|TO DO |WAIT|IN PROGRESS|DONE|
|-------|-|-|-|
|Player move|||X|
|Bombe|||X|
|Wall|||X|
|Bricks|||X|
|Teleporter|||X|
|Switch|||X|
|Mine|||X
|Submarine animation|||X
|Water player animation|||X
|Player in trolley |||X
|Trolley move |||X
|Player carry bombe (bonus)|||X
|Player throw bombe (bonus)|||X
|BAD Bomber|||X
|Display score|||X
|Count down end game||X|
|Restart Game after time||X|
|Network protocol |X||
|Client viewer |X||
|IA LVL1 |X||
|IA LVL2 |X||
|IA LVL3 |X||
|IA LVL4 |X||
|IA LVL5 |X||
|Sound animation||X|


