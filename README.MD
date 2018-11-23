将普通Android App改造成系统APP,让APP具有系统权限和使用系统framework.jar里面的@hide的API,步骤如下<br>
<1> : 拷贝framework.jar包:<br>
~\out\target\common\obj\JAVA_LIBRARIES\framework_intermediates文件目录下的classes.jar<br>
<2> : 将classes.jar改名成framework.jar拷贝到APP工程的libs目录下,然后选中鼠标右击"Add as library..."<br>
<3> : 然后File->Project Structure -> App -> dependencies,将framework.jar的implementation改成compile only<br>
<4> : 然后在工程根目录下修改build.gradle<br>
allprojects {<br>
    ...<br>
    gradle.projectsEvaluated {<br>
        tasks.withType(JavaCompile) {<br>
            options.compilerArgs.add('-Xbootclasspath/p:app/libs/framework.jar')<br>
        }<br>
    }<br>
}<br>
<5> : 修改APP目录下的build.gradle,将下面一行删除<br>
implementation fileTree(include: ['*.jar'], dir: 'libs')<br>
libs下面的文件如果有其他的包需要导入,就一个一个的导入,不要使用通配符的方式导入<br>
并且添加:<br>
preBuild {<br>
    doLast {<br>
        def imlFile = file(project.name + ".iml")<br>
        println('Change ' + project.name + '.iml order')<br>
        try {<br>
            def parsedXml = (new XmlParser()).parse(imlFile)<br>
            def jdkNode = parsedXml.component[1].orderEntry.find { it.'@type' == 'jdk' }<br>
            parsedXml.component[1].remove(jdkNode)<br>
            def sdkString = "Android API " + android.compileSdkVersion.substring("android-".length()) + " Platform"<br>
            new groovy.util.Node(parsedXml.component[1], 'orderEntry', ['type': 'jdk', 'jdkName': sdkString, 'jdkType': 'Android SDK'])<br>
            groovy.xml.XmlUtil.serialize(parsedXml, new FileOutputStream(imlFile))<br>
        } catch (FileNotFoundException e) {<br>
            // nop, iml not found<br>
        }<br>
    }<br>
}<br>
<br>
<6> : Manifest文件中增加android:sharedUserId="android.uid.system",增加到系统进程.<br>
<7> : 系统签名,参考keystore路径下的readme文件.<br>
<8> : 好了,整个APP可以为所欲为了,并且可以用android studio随意干事了.<br>