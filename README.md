# SkinGallery
一款基于GalleryFinal的图片选择框架，解决原框架内存泄漏的问题

Step 1：Add it in your root build.gradle at the end of repositories:
	allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}
Copy
Step 2：Add the dependency：
	dependencies {
	        compile 'com.github.dida-logistics:SkinGallery:1.0.0'
	}
