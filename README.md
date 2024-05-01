# Release notes.

./mvnw clean
./mvnw build o ./mvnw install

Agregar accesos y claves de la cuenta del repositorio ossrh
ver instrucciones para obtener el token
Se tienen que guardar en el archivo ~/m2/settings.xml
Hay que importar las claves de gpg

    gpg --import myprivate.key
    gpg --import mypub.key

Al importar solicita la contraseña

Se pueden ver las claves agregadas con el comando
gpg --list-secret-keys --keyid-format=long

El siguiente comando cambia la versión.
Importante escribir el comando con la versión que corresponda

./mvnw versions:set -DnewVersion=1.2.3

El siguiente comando hace el despligue y el release

./mvnw clean deploy -P release

Si hay error de gpg: signing failed: Inappropriate ioctl for device

ejecutar los giuientes comandos en la misma terminal

GPG_TTY=$(tty)
export GPG_TTY
