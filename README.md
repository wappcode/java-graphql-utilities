# Release notes.

Leer todo el documento antes de iniciar el proceso de deploy

NOTA: El último despliegue se hizo modificando el pom.xml de la carpeta gql-utilities y ejecutando los comandos dese esa carpeta sin incluir -pl gql-utilities -am  (Solo graphql-app se puede ejecutar)

./mvnw clean
./mvnw compile o ./mvnw install
./mvnw clean install -pl gql-utilities -am (para instalar la libreria gql-utilities)

Agregar accesos y claves de la cuenta del repositorio ossrh
ver instrucciones para obtener el token (https://central.sonatype.org/publish/generate-token/)
Se tienen que guardar en el archivo ~/m2/settings.xml
Hay que importar las claves de gpg

    gpg --import myprivate.key
    gpg --import mypub.key
    gpg --import-ownertrust trustfile.gpg

Al importar solicita la contraseña

Se pueden ver las claves agregadas con el comando
gpg --list-secret-keys --keyid-format=long

El siguiente comando cambia la versión.
Importante escribir el comando con la versión que corresponda

./mvnw versions:set -DnewVersion=1.0.8 -pl gql-utilities -am

El siguiente comando hace el despligue y el release 

./mvnw clean deploy -DperformRelease=true -P release -pl gql-utilities -am (Pide la contraseña o clave gpg)

alternamtivamente asignado la contraseña al comando

./mvnw clean deploy -DperformRelease=true -Dgpg.passprhase="gpg_prhasse" -P release -pl gql-utilities -X

Si hay error de gpg: signing failed: Inappropriate ioctl for device

ejecutar los giuientes comandos en la misma terminal

GPG_TTY=$(tty)
export GPG_TTY

Despues del deploy hay que ingresar a https://s01.oss.sonatype.org/

Y en staging repositories hay seleccionar y cerrar el repositorio despues que el proceso de cierre concluya (no es inmediato puede tomar algunos minutos) hay que seleccionarlo nuevamente y presionar el botón release

## Notas adicionales

Se movio el archivo release.properties a la carpeta raiz de gql-utilities. (verificar que esto no impida hacer el deploy)

## Las páginas con información para deploy son

https://maven.apache.org/repository/guide-central-repository-upload.html
https://central.sonatype.org/publish/publish-guide/
https://central.sonatype.org/publish/publish-maven/

## ejemplo archivo ~/.m2/settings.xml

El dato de usuario y contraseña se obtiene de la página https://s01.oss.sonatype.org/ 
En la parte superior en el nombre de usuario logueado dar click en el menu y seleccionar perfile y des pues seleccionar UserToken (ver instrucciones en  https://central.sonatype.org/publish/generate-token/)
```
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd">
  <localRepository/>
  <interactiveMode/>
  <offline/>
  <pluginGroups/>
  <servers>
<server>
  <id>ossrh</id>
  <username>orssh_username</username>
  <password>orssh_password</password>
</server>
  </servers>
  <mirrors/>
  <proxies/>
  <profiles/>
  <activeProfiles/>
</settings>
```
