# BMYBBS IndeS

这是一个用于 [BMYBBS](https://github.com/bmybbs/bmybbs) 索引、搜索内容的库。

原本 BMYBBS 使用 Python 脚本调用 [PyLucene](https://lucene.apache.org/pylucene/index.html)，使用二进制格式读取 `.BOARDS` 以及 `.DIR` 文件。这有一些弊端：

1. PyLucene 是通过 [JCC](https://lucene.apache.org/pylucene/jcc/index.html) 将 [Lucene](https://lucene.apache.org/core/) Java 版本转编译出 Python 接口。脚本开发时候的 PyLucene 版本过旧（2014 年[升级到 3.5](https://github.com/bmybbs/bmybbs/issues/61)），未必在 Python 3.x 支持，而 Ubuntu 20.04 系统下下尝试编译 PyLucene 8.8.1 时 `make test` 并未全部通过。
2. 除去 PyLucene 本身安装的复杂，Python 脚本对于二进制数据的解析也是一个隐患，一旦数据格式发生变化，脚本也要随之变更。
3. PyLucene 本身执行依然依赖 Lucene Java 版本。

基于以上考虑，本项目目标：

1. 通过 [JNI](https://docs.oracle.com/javase/8/docs/technotes/guides/jni/) 方式调用 BMYBBS 原生的库（`libytht` `libbmy` `libythtbbs`）来访问和转换数据，直接交由 Lucene 处理。
2. 以调用 jar 的方式创建索引、搜索内容。

## 使用

假设 `bmybbs-indes-${version}.jar` 已经和其依赖的 `*.jar` 放在同一个目录下。

### 索引

```bash
LD_LIBRARY_PATH=/path/to/dir/for/libbmybbs-java.so java -cp "/path/to/dir/for/jars" edu.xjtu.bmybbs.App index <board_name> <delta_days>
```

其中 `<board_name>` `<delta_days>` 都是参数，分别有两个魔数：
* `ALL` 表示所有版面
* `-1` 表示所有时间段。

例如：

```bash
# 初始化所有索引
LD_LIBRARY_PATH=/path/to/dir/for/libbmybbs-java.so java -cp "/path/to/dir/for/jars" edu.xjtu.bmybbs.App index ALL -1

# 新增过去 24h 内的索引
LD_LIBRARY_PATH=/path/to/dir/for/libbmybbs-java.so java -cp "/path/to/dir/for/jars" edu.xjtu.bmybbs.App index ALL 1
```

### 查询

```bash
LD_LIBRARY_PATH=/path/to/dir/for/libbmybbs-java.so java -cp "/path/to/dir/for/jars" edu.xjtu.bmybbs.App search <board_name> <query>
```

例如：

```bash
LD_LIBRARY_PATH=/path/to/dir/for/libbmybbs-java.so java -cp "/path/to/dir/for/jars" edu.xjtu.bmybbs.App search XJTUnews 西安交通大学
```

输出 JSON 格式的查询结果到 `stdout`，UTF8 编码。数组中的每一项包括：

* `board`: 版面
* `title`: 标题
* `owner`: 作者
* `timestamp`: 文章时间，适用于 term / www 方式搜索，有传统阅读模式
* `thread`: 主题时间，适用于默认主题阅读模式的情况

