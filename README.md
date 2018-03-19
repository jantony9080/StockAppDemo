# StockAppDemo
### Running instructions.
1. Download or clone the project.
2. Change to the root project directory.
3. Build the project using gradle wrapper:
```bash
./gradlew :api:bootJar
```
On windows use
```bash
gradlew.bat :api:bootJar
```
4. Change to the <project root>/api/build/libs
5. Run the jar:
```bash
  java -jar stock-app-0.1.0.jar
  ```

### API documentation.
The API documentation in swagger format can be found under the following URL:
http://localhost:8080/swagger-ui.html

Or in case you run the app on different host, you should replace the host name with the one where app is actually running.

If something isn't working or you have any questions, feel free to contact me.
