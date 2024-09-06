# cp -r ./.idea/{dataSources.xml,dataSources.local.xml,dataSources} ~/OneDrive/config/jetbrain
# cd ~/Library/Application\ Support/JetBrains/IntelliJIdea2023.3/consoles/
# cp -r ./db/* ~/OneDrive/config/jetbrain/consoles
# rm -r ./db
# ln -s ~/OneDrive/config/jetbrain/consoles db

ln -s $HOME/OneDrive/config/jetbrain/dataSources dataSources
ln -s $HOME/OneDrive/config/jetbrain/dataSources.xml dataSources.xml
ln -s $HOME/OneDrive/config/jetbrain/dataSources.local.xml dataSources.local.xml

ln -s $HOME/OneDrive/config/jetbrain/consoles db
