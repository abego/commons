# CHANGELOG

All notable changes to this project will be documented in this file.

This project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).
Breaking changes are written in **bold**.

## [Unreleased]

## [1.1.0] - 2023-10-14

Re-release, same contents as 1.0.0.
(Publish process to Nexus had problems, 1.0.0 was removed)

## [1.0.0] - 2023-10-09

### Added

#### Packages

- lineprocessing

#### Classes

- ClassLoaderUtil
- DateUtil
- FormattedTextProcessorForHTML
- IORunnable
- ProcessUtil
- SeparatedItemScanner
- SystemTesting

#### Methods

- ArrayUtil.concatenate
- ArrayUtil.firstOrNull
- FileUtil.allFilesExist
- FileUtil.copyFilesDeep
- FileUtil.copyFilesInDirectoryDeep
- FileUtil.copyResourcesDeep
- FileUtil.copyResourcesInLocationDeep
- FileUtil.filePathLines
- FileUtil.filesInDirectory
- FileUtil.filesInDirectoryAndDeeper
- FileUtil.parseFiles (replaces filesFromFilePathes)
- FileUtil.withFilesInDirectoryAndDeeperDo
- InputStreamUtil.copyStream
- IterableUtil.firstOrNull
- JUnit5Util.assertEqualFiles
- JUnit5Util.assertEqualLines
- ListUtil.splitInBatches
- LoggerUtil.logStringsAsWarnings
- ServiceLoaderUtil.loadService // new overloads
- ServiceLoaderUtil.loadServices // multiple overloads
- StringUtil.escapeForHtml
- StringUtil.indent
- StringUtil.lineCount
- StringUtil.lines
- StringUtil.prefixBefore
- StringUtil.removePrefix
- StringUtil.slashesToDots
- StringUtil.sortedUnixLines
- StringUtil.unixString
- ThrowableUtil.messageOrToString
- URLUtil.asJarProtocolURL

### Changed

- PropertiesGroup: customizable sharedGroupName and configDirectory
- IterableUtil.appendTextOf allows `null` in items
- **FileUtil.copyResourcesToDirectory has additional "Class" parameter**
  - because of fix: copyResourcesToDirectory/copyResourcesToDirectoryFlat fails
    when running on Java 17
- **FileUtil.copyResourcesToDirectoryFlat has additional "Class" parameter**
  - because of fix: copyResourcesToDirectory/copyResourcesToDirectoryFlat fails
    when running on Java 17
- **ReaderUtil.reader(File, Charset) throws _IOException_**

### Deprecated

- class LineProcessor
- method InputStreamUtil.readLineWise

### Removed

- **package stringpool**
- **method FileUtil.filesFromFilePathes**
- **method URLUtil.toFile(URL)**

### Fixed

- FileUtil: error when absoluteResourceDirectoryPath/resourceNamePrefix in
  copyResourcesToDirectory/copyResourcesToDirectoryFlat does not end with "/"
- FileUtil: copyResourcesToDirectory/copyResourcesToDirectoryFlat fails
  when running on Java 17
- PropertiesGroup: getProperty may return null (now defined as @Nullable)

## [0.12.0] - 2023-03-06

### New

#### Submodules

- progress

#### Types

- AssertRetrying/Service
- FormattedText#FontStyle
- IOFunction
- IOFunctionUtil
- LineProcessor
- Polling/Service
- PredicateUtil
- PropertiesGroup
- PropertiesIOUtil
- SimpleSet
- StackTraceElementUtil
- StringEvaluatorUtil
- Timeouts/TimeoutSupport
- UncheckedFileNotFoundException
- URIUtil
- VarNullable

#### Methods

- ArrayUtil#allButLastItem
- ArrayUtil#firstOrNull
- FileUtil#canonicalPath
- FileUtil#filesFromFilePathes
- FileUtil#findExistingDirectory
- FileUtil#copyFile
- FileUtil#emptyFile
- FileUtil#findExistingDirectory
- FileUtil#mkdirs
- FileUtil#removeFileExtension
- FormattedText#toPlainText()
- FormattedTextUtil#withStyledRanges
- FormattedTextUtil#withStyledRangesIfRequired
- InputStreamUtil#readLineWise
- IterableUtil#appendTextOf
- IterableUtil#asSortedLines(Iterable<T>, Function<T,String>)
- ListUtil#toList(Iterable)  // generalized
- ObjectUtil#allAreNotNull
- ObjectUtil#compareAsTexts
- ObjectUtil#instanceOfOrNull
- ObjectUtil#valueOrFail
- PrintStreamUtil#newPrintStreamToBufferedFile
- PropertiesIOUtil#addProperties
- PropertiesIOUtil#readProperties/readPropertiesGroup
- PropertiesUtil: add access to some SystemProperties
- Seq#allItemsMatch
- Seq#first(Predicate)
- Seq#firstOrNull()
- Seq#firstOrNull(Predicate)
- Seq#getSize()
- Seq#hasItemWith(Predicate)
- Seq#indexOfFirst(Predicate)
- Seq#itemOrElse
- Seq#itemOrNull
- Seq#last()
- Seq#noItemMatches
- Seq#rest()
- Seq#sortedByText
- SeqUtil#newSeq(Enumeration)
- SeqUtil#newSeq(Function<T,R> mapper, T... items)
- SeqUtil#newSeq(Iterable)
- SeqUtil#newSeq(mapper, int...)
- SeqUtil#newSeqUniqueItems
- SeqUtil#sortedByText
- SeqUtil#toCompactString(Seq<?>)
- SeqUtil#toSeq(Iterable)
- SetUtil#asSet(T...)
- StringUtil#compareToIgnoreCaseStable
- StringUtil#containsAnyOf
- StringUtil#endsWithIgnoreCase
- StringUtil#lineCount
- StringUtil#newIncludesStringPredicate
- SystemUtil#isMacOS
- VarUtil#newVar(T initialValue)
- VLQUtil#writeVLQInt/readVLQInt/readVLQIntArray

### Changes (Possibly incompatible)

- Move Seq#rest -> SeqUtil#rest
- Remove StringUtil#characters(String):Seq (now internal in DiffImpl)
- StringUtil#lines returns String[] (not Seq<String>)
- TimeoutService... (not ...Support...)

### Bug Fixes

- StringPoolBuilder#addJoined does not handle `null` cases correctly

### Deprecation

- PollingUtil (use Polling instead)
- URLUtil#toFile(URL) (use FileUtil#toFile(URL))
- StringPool module (use https://github.com/abego/abego-stringpool instead)

### Improvements

- AbstractSeq#toString: use stopLength feature to limit the toString length
- Seq: better support for Sets
- StringUtil#join: support null elements
- StringUtil#joinWithEmptyStringForNull: support null elements
- VLQUtil: support signed int and (unsigned) long encoding/decoding
- removed dependency to "org.abego.guitesting"
- improved build/release process (e.g. no signing by default)
- tuning (avoid boxing, better implementations, ...)
- documentation
- reformatting
- more tests
- reduce warnings

## [0.11.0] - 2021-04-09

### New

#### Types

- LoggerUtil
- SPI (Annotation)

#### Methods

- FileUtil#absolutePath
- FileUtil#copyResourcesToDirectory
- FileUtil#copyResourcesToDirectoryFlat
- FileUtil#existingFileOrNull
- FileUtil#pathRelativeTo
- FileUtil#requireDirectory
- FileUtil#stripExtension
- FileUtil#writeText returns File
- ObjectUtil#valueOrFail
- OutputStreamUtil#getOutputStreamFailingOnWrite
- PrintStreamUtil#newPrintStreamToNullDevice
- StringUtil#firstLine
- URLUtil#isURI
- URLUtil#isURL
- URLUtil#urlDecode,
- URLUtil#textOf(URL)
- URLUtil#toFile
- Var#getOrNull
- Var#isEditable
- Var#mapOrElse
- Var#mapOrNull
- VarUtil.newTextFileVar

### Bug Fixes

- "InterruptedException" ignored in BooleanSupplierUtil#waitUtil

### Improvements

- PollingUtil: Interrupting a Thread that is running poll forces early timeout
- Hide implicit public constructor of util class TextDiff
- Use StringBuilder instead of StringBuffer in DiffImpl
- Remove unnecessary cast to "IOCommand" in FileUtil#ensureFileExists.
- increase code coverage
- Improved JavaDoc

## [0.10.0] - 2020-06-03

### New

#### Types

- AbstractSeq
- AlignedItemPair
- BooleanSupplierUtil
- BooleanUtil
- BufferedReaderUtil
- ByteConsumer
- ByteConsumerForOutputStream
- ByteSupplier
- ByteUtil
- CommandLineParser
- CommandLineParserDefault
- DiffImpl
- Difference
- DifferenceBuilder
- DifferenceDefault
- EmptySeq
- FileCannotBeDeletedException
- FileDiffUtil
- FormattedText
- FormattedTextProcessor
- FormattedTextUtil
- IDUtil
- IntPair
- IntPairDefault
- IntRange
- IntRangeBuilder
- IntRangeDefault
- IntUtil
- IteratorUsingAccessor
- JSONPointer
- LocaleUtil
- LongUtil
- MapUtil
- MappedSeq
- PathUtil
- PrintStreamUtil
- PropertiesIOUtil
- ResourceBundleSpecifierFactory
- ResourceBundleSpecifierFactoryDefault
- ResourceUtil
- RuntimeUtil
- SeqForIterable
- SeqNonEmptyUtil
- SeqNonEmptyWithAppended
- SeqNonEmptyWithAppendedDefault
- SeqUtil
- SequenceDiff
- SequenceDiffDefault
- ServiceLoaderUtil
- StreamUtil
- StringPool
- StringPoolBuilder
- StringPoolBuilderDefault
- StringPoolDefault
- TextDiff
- ToStringBuilder
- TreeNode
- TreeNodeDefault
- TreeNodeLazy
- TreeNodeUtil
- URLUtil
- VLQUtil
- Var
- VarUtil
- Writable

#### Methods

- ArrayUtil#checkArrayIndex
- ArrayUtil#contains(T[],T)
- ArrayUtil#indexOf(T[],T)
- ArrayUtil#itemOrDefault(T[] array, int index, T defaultValue)
- ClassUtil#classNameOrNull(Object)
- ClassUtil#newInstanceOfClassNamed, Seq#sorted(Comparator)
- ClassUtil#packagePath
- CollectionUtil#addAll
- FileUtil#appendText
- FileUtil#deleteFile
- FileUtil#ensureFileExists
- FileUtil#runIOCode
- FileUtil#textOfFileIfExisting
- FileUtil#toURL(File), FileUtil#toFile(URL), PathUtil#toURL(Path)
- FileUtil#writeText
- InputStreamUtil#newInputStream
- IntRangeDefault#toString/hashCode/equals
- IterableUtil#appendTextOf
- IterableUtil#firstOrNull
- IterableUtil#isEmpty(Iterable<?> iterable)
- IterableUtil#join(CharSequence, Iterable) // alias of IterableUtil#textOf(Iterable, CharSequence)
- IterableUtil#size
- IterableUtil#toIterable(T...)
- IterableUtil#toStringOfIterable
- JUnit5Util#assertIntRangeEquals JUnit5Util#castOrFail
- JUnit5Util#assertThrowsWithMessage
- ListUtil#toList(Object...), ListUtil#toList(Iterable)
- ListUtil#toMappedList|map
- ListUtil#toSortedList
- ListUtil#toSortedList(Iterable)
- ObjectUtil#valueOrElse
- ObjectUtil#valueOrElse
- PathUtil#ensureDirectoryExists
- PrintStreamToBuffer#newPrintStreamToBuffer
- ResourceUtil#hasResource
- Seq#newSeq(Iterable<T> iterable)
- Seq#seqsAreEqual
- Seq#sortedBy|sorted
- Seq#stream default implementation
- SeqUtil#filter
- SeqUtil#map
- SeqUtil#newSeqOfNullable
- SeqUtil#reverse
- SeqUtil#sortedBy|sorted
- ServiceLoaderUtil#loadService
- StreamUtil#toStream(Object...)
- StringUtil#DEFAULT_LOCALE
- StringUtil#arrayNullable
- StringUtil#camelCased/dashCased/snakeCased/snakeUpperCased
- StringUtil#htmlEscaped, ThrowableUtil#allMessages...
- StringUtil#limitString(String)
- StringUtil#limitString(String, int)
- StringUtil#quoted2
- StringUtil#repeat
- StringUtil#splitWhitespaceSeparatedString(String)
- StringUtil#substringSafe
- StringUtil#toArray
- StringUtil#withLineSeparatorsForNewlines
- ThreadUtil#runInNewThread
- UncheckedException#newUncheckedException

### Changes (Possibly incompatible)

- Move ClassUtil.textOfResource -> io.ResourceUtil
- Move FileUtil/PrinterWriterUtil.write -> WriterUtil.write
- Move PropertiesUtil -> io.PropertiesIOUtil
- Move ResourceBundleSpecifier.resourceBundleSpecifier -> ResourceBundleUtil.newResourceBundleSpecifier
- Move Seq.emptySeq -> SeqUtil
- Move Seq.newSeq -> SeqUtil
- Move SeqNonEmpty.seqNonEmpty... -> SeqNonEmptyUtil
- Move SeqNonEmpty.with -> SeqNonEmptyUtil.newSeqNonEmpty
- Move and Hide Seq.seqsAreEqual -> SeqUtil
- NoThrows: FileDiffUtil#directoryDifferences
- NoThrows: FileUtil#copyResourceToFile
- NoThrows: FileUtil#directory
- NoThrows: FileUtil#ensureDirectoryExists
- NoThrows: FileUtil#normalFile
- NoThrows: FileUtil#setReadOnly
- NoThrows: FileUtil#tempDirectoryForRun
- NoThrows: FileUtil#tempFileForRun
- NoThrows: FileUtil#tempFileForRunFromResource
- NoThrows: FileUtil#textOf
- NoThrows: FileUtil#textOfFile
- NoThrows: InputStreamUtil#write
- NoThrows: RuntimeUtil#execAndReturnOutAndErr
- NoThrows: ScannerUtil#textOf
- NoThrows: WriterUtil#write
- Other: use Eclipse NotNull annotation (not JSR-305) to avoid Java 9 issues
- Remove "Serialization" support
- Remove ArrayUtil#array(T...); add StringUtil#array(String...)
- Remove Blackboard#newBlackboard (and loadService-Configuration)
- Remove ComparableUtil
- Remove ObjectUtil#requireNonNull
- Remove PrintStreamToBuffer#printStreamToBuffer
- Remove ResourceBundleUtil#newResourceBundleSpecifier (and loadService-Configuration)
- Remove StringPool#NULL_ID (use 0 instead)
- Remove UncheckedException#uncheckedException (use UncheckedException#newUncheckedException instead)
- Remove default implementation for Seq.map/Seq.filter, implementations may use SeqUtil.map/SeqUtil.filter
- Rename CommandLine -> CommandLineParser
- Rename CommandLineDefault -> CommandLineParserDefault
- Rename CommandLineParserDefault#newCommandLineDefault -> ...#newCommandLineParserDefault
- Rename ExtendingSeqNonEmpty -> SeqNonEmptyWithAppended
- Rename ExtendingSeqNonEmpty#extendedBy -> SeqNonEmptyWithAppended#appended
- Rename ExtendingSeqNonEmptyDefault -> SeqNonEmptyWithAppendedDefault
- Rename ResourceBundleSpecifier#bundleBaseName|language|country|platform|fileExtension -> ...#get{PropertyName}
- Rename ResourceBundleSpecifierDefault#bundleBaseName|language|country|platform|fileExtension -> ...#get{PropertyName}
- Rename ResourceBundleSpecifierDefault#resourceBundleSpecifier -> ...newResourceBundleSpecifier
- Rename ResourceBundleSpecifierDefaultFactory -> ResourceBundleSpecifierFactoryDefault
- Rename UUIDUtil -> UDUtil
- ReturnType: long Seq.size() to int Seq.size()
- final: Util classes

### Bug Fixes

- "The type package-info is already defined" (reported by Eclipse)
- Cyclic dependency between Blackboard and BlackboardDefault
- dependency cycle related to "Seq"
- FileUtil#ensureFileExists does not check result of createNewFile
- IntUtil#ints(int) throws wrong exception when argument is <= 0
- PrintStreamUtil#appendLines does not close BufferedReader
- SeqNonEmptyWithAppended#appended fails when used twice on same Seq
- StringPoolDefault Iterators don't throw NoSuchElementException
- StringUtil#splitWhitespaceSeparatedString ignores trailing whitespaces
- Unclear error message in ClassUtil#newInstanceOfClassNamed when instance is not of expected type
- VLQUtil fails for encodings of numbers > Integer#MAX_VALUE
- WriterUtil#writer fails when file does not exist.
- class FullyQualifiedName is final but extended by ClassName

### Deprecated

- FileUtil#write(...)
- ResourceBundleSpecifier#resourceBundleSpecifier
- UncheckedException#uncheckedException

### Improvements

- Various improvements, for details see Git log ("Improve:...")

## [0.9.5] - 2019-02-24

### New

- Support Sonatype Nexus server

### Improvements

- Ignore Sonarlint issuestore
- More details in README

### Changes (Possibly incompatible)

- Use IDEA jdk

### Bug Fixes

- Fix JavaDoc issues

## [0.9.4] - 2019-02-19

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

## [0.9.3] - 2018-12-28

- Initial Release
