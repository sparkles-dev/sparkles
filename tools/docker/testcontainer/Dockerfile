FROM angular/ngcontainer:latest

###
# Gradle
RUN sudo mkdir -p /opt/gradle \
    && sudo curl -o ~/gradle-4.10.2-bin.zip https://downloads.gradle.org/distributions/gradle-4.10.2-bin.zip \
    && sudo unzip -d /opt/gradle ~/gradle-4.10.2-bin.zip \
    && sudo rm ~/gradle-4.10.2-bin.zip \
    && echo "export PATH=/opt/gradle/gradle-4.10.2/bin:$PATH" >> ~/.profile
