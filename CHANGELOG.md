# CHANGELOG

## 0.9.4

### New

#### Types

- Blackboard
- IterableUtil
- PollingUtil
- Seq
- SeqNonEmpty
- SeqUtil
- StringEvaluator
- ThreadUtil
- Timeoutable
- TimeoutSupplier
- TimeoutUncheckedException


#### Methods

- ArrayUtil#iterator(T[])
- ClassUtil#resource(Class, String)
- ListUtil#list(T...)
- ListUtil#nthItemAsStringOrNull(List<?>, int) 
- ObjectUtil#checkType(Object, Class)
- PrintWriter#printWriter
- StringUtil#appendEscapedChar
- StringUtil#appendEscapedString
- StringUtil#escaped
- StringUtil#join
- StringUtil#join_emptyNull
- StringUtil#quoted
- StringUtil#singleQuoted


### Improvements

- Sonar and JaCoCo support added
- Warnings fixed
- Increased code coverage
- Improved Documentation 
- Unused code removed
- Nullable annotations used
- Renames (in internal and test code)

### Changes (Possibly incompatible)

- MustNotInstantiateException.mustNotInstantiateException() deprecated.

- CharArrayRange now final
- ClassName now final
- MustNotInstantiateException now final
- PrintStreamToBuffer now final
- RunOnClose and RunOnCloseUtil now final and in "org.abego.commons.lang"
- ToFileInHeaderLineWriter now final


### Bug Fixes

- ToFileInHeaderLineWriter fails when directory for output is missing


## 0.9.3

- Initial Release
