<p align="center"><img src="https://github.com/joanascheerzup/ProjetoFinalCatalisa/blob/main/app/src/main/res/drawable/logo_ezuppers.png" alt="Logotipo do applicativo com a palavra E-zuppers em azul escuro. Abaixo da imagem, uma linha com cores do arco-íris e os dizeres - conectando Zuppers pelo mundo -"></p>

<h1>E-Zuppers</h1>
<h2>Descrição do Projeto</h2>
O E-Zuppers, é uma rede social com o objetivo de promover a conexão e interação entre os zuppers.</p>

<h2>Funcionalidades do Aplicativo</h2>

1- Cadastro de Usuário;  
2- Login;  
3- Criação de post;  
4- Filtro de usuários por estado e cidade;  
5- Like de post's;  
6- Lista de favoritados;  
7- Perfil do Usuário;  

<h2>Tecnologia</h2>
A Linguagem utilizada foi o Kotlin para o desenvolvimento do aplicativo   <img width="50" src="kotlin.png"> 
  
  <h2> Versão do Projeto: 1.0.0 - Localizada na MAN</h2>
<h2>Padrão de Arquitetura</h2>
 Model, View, ViewModel-MVVM</p>
 
<h2>Dependencias utilizadas no projeto: </h2>   

**Navigation**   
implementation("androidx.navigation:navigation-fragment-ktx:2.5.1")  
implementation("androidx.navigation:navigation-dynamic-features-fragment:2.5.1")  
implementation("androidx.navigation:navigation-ui-ktx:2.5.1")
  
  **Lifecycle**  
  implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1'  
   implementation 'androidx.annotation:annotation:1.4.0'  
   implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.5.1'     
   implementation 'androidx.recyclerview:recyclerview:1.2.1'    
   </br>
   **Gson**    
   implementation 'com.google.code.gson:gson:2.9.0'  
    implementation "com.squareup.retrofit2:converter-gson:2.9.0"  
    </br>
   **Network Connections**    
   implementation "com.squareup.retrofit2:retrofit:2.9.0"  
    implementation 'com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.10'  
    </br>
     **Picasso**  
     implementation 'com.squareup.picasso:picasso:2.71828'  
     </br>
      **Firebase**  
      implementation platform('com.google.firebase:firebase-bom:30.3.0')  
      implementation 'com.google.firebase:firebase-analytics-ktx'  
      implementation 'com.google.firebase:firebase-database-ktx'  
      implementation 'com.google.firebase:firebase-messaging-ktx'   
      implementation 'com.google.firebase:firebase-auth-ktx:21.0.6'  
      </br>
      **Room Database**   
      implementation("androidx.room:room-runtime:2.4.3")  
          kapt("androidx.room:room-compiler:2.4.3")  






