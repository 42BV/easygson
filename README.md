EasyGson
========
With Json-oriented databases, like MongoDB, and heavy usage of Json to feed Javascript-powered frontends, Json-all-the-way architectures becomes ever more popular.

Java can work against this architecture, because there is no native Json handling in Java. The Java convention is that you transform your Json into a Java domain model. Both Gson and Jackson provide excellent ways to automatically map your Json to a Java domain model and vice versa. This is a fine way to work. However, let us suppose you do not want this. You want Json-all-the-way. You may have a problem in that case.

The aim of EasyGson is to help you out if you want to process pure Json, without transforming into a Java domain model. Design goals:

* power — you know what you are doing and you don't need Java to protect you
* fluency — use a fluent interface, just like you would in JavaScript (understand, though, it will never be as sweet)
* ease of use — must be extremely simple to use
* giant's shoulders — Gson powers EasyGson, EasyGson just makes it easier to use

In order to use EasyGson (and therefore Gson) in your project, simply add the following dependency:

```xml
<dependency>
    <groupId>org.easygson</groupId>
    <artifactId>easygson</artifactId>
    <version>1.0.0</version>
</dependency>
```

Getting Started
---------------
Let's assume you have the following JSON document:
```json
{
    "chapters" : [
        {
            "title" : "Chapter I",
            "paragraphs" : [
                "Lorem Ipsum and so forth...",
                "... in the middle...",
                "... final text"
            ]
        },
        {
            "title" : "Chapter I",
            "paragraphs" : [
                "Epilogue..."
            ]
        }
    ]
}
```

You can instantiate a JsonEntity with this JSON document in the following way:
```java
JsonEntity json = new JsonEntity(
    "{\"chapters\":[{\"title\":\"Chapter I\",\"paragraphs\":[\"Lorem Ipsum"+
    " and so forth...\",\"... in the middle...\",\"... final text\"]},"+
    "{\"title\":\"Chapter II\",\"paragraphs\":[\"Epilogue...\"]}]}");
```

If you want to loop the double array and print the values, this would be the way to do it:
```java
for (JsonEntity chapter : json.get("chapters")) {
    System.out.println(chapter.asString("title"));
    for (JsonEntity paragraph : chapter.get("paragraphs")) {
        System.out.println(paragraph.asString());
    }
}
```

A JSON object can be built from scratch using the fluent API:
```java
JsonEntity json = emptyObject()
    .createArray("chapters")
        .createObject()
            .create("title", "Chapter I")
            .createArray("paragraphs")
                .create("Lorem Ipsum and so forth...")
                .create("... in the middle...")
                .create("... final text")
                .parent()
            .parent()
        .createObject()
            .create("title", "Chapter II")
            .createArray("paragraphs")
                .create("Epilogue...")
                .parent()
            .parent()
        .parent();
```

You can use the loop routine from above to check if it works.

It is possible to specifically mention paths that will be traversed:
```java
System.out.println(json.get("chapters").get(1).get("paragraphs").asString(0));
```

Results in
```text
Epilogue...
```

If you somehow call the wrong method on a JSON element, an exception will show you the way:
```java
json.get("chapters").get(1).get("paragraphs").get(
    "property-that-does-not-exist-on-an-array");
```

```text
chapters[1].paragraphs is not an object, so the property could not be fetched
```


License
-------
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

Sponsor
-------
This component was donated by [42 BV](http://www.42.nl) ![42 logo](http://www.42.nl/images/42-54x59.png "42")