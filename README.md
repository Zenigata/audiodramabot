<h1 align="center">Welcome to Drama 👋</h1>
<p>
  <a href="https://www.gnu.org/licenses/gpl-3.0.fr.html" target="_blank">
    <img alt="License: GPLv3" src="https://img.shields.io/badge/License-GPLv3-yellow.svg" />
  </a>
  <a href="https://twitter.com/JohanBonneau" target="_blank">
    <img alt="Twitter: JohanBonneau" src="https://img.shields.io/twitter/follow/JohanBonneau.svg?style=social" />
  </a>
</p>

> A Discord bot to find and listen to audio drama.

Commandes disponibles :
* `!fic-about` donne des informations sur ce bot.
* `!fic-find nom` renvoie la première fiction trouvée contenant ce nom (⚠️ aux accents).
* `!fic-random` renvoie le détail d'une fiction aléatoire.
* `!fic-play nom S01 E02` joue en vocal l'épisode 2 de la saison 1.
* `!fic-pause` suspend ou reprend l'épisode écouté.
* `!fic-stop` arrête et expulse le bot du canal vocal.

## Install

### Add airtable.java jar located in /lib
    mvn install:install-file -Dfile=lib/airtable.java-0.2.2.jar -DgroupId=sybit-education -DartifactId=airtable.java -Dversion=0.2.2 -Dpackaging=jar -DgeneratePom=true

### Create credentials.properties in src/main/resources folder
    AIRTABLE_API_KEY=

## Usage

[Add the bot](https://discordapp.com/oauth2/authorize?client_id=811029116987768862&scope=bot).

## Special thanks

* https://github.com/timlg07/Mr-Turing
* https://github.com/Kaysoro/KaellyBot
* https://github.com/Shadorc/Shadbot


## Author

👤 **Johan Bonneau**

* Website: http://zenigata.fr
* Twitter: [@JohanBonneau](https://twitter.com/JohanBonneau)
* Github: [@Zenigata](https://github.com/Zenigata)

## Show your support

Give a ⭐️ if this project helped you!

## 📝 License

Copyright © 2021 [Zenigata](https://github.com/Zenigata).<br />
This project is [GPLv3](https://www.gnu.org/licenses/gpl-3.0.fr.html) licensed.

***
_This README was generated with ❤️ by [readme-md-generator](https://github.com/kefranabg/readme-md-generator)_
