#if defined(_WIN32) || defined(_WIN64)
    #define int64_t __int64
    #define uint64_t unsigned __int64
#endif

#ifdef _MACOSX
	typedef unsigned long   GLenum;
	typedef unsigned char   GLboolean;
	typedef unsigned long   GLbitfield;
	typedef signed char     GLbyte;
	typedef short           GLshort;
	typedef long            GLint;
	typedef long            GLsizei;
	typedef unsigned char   GLubyte;
	typedef unsigned short  GLushort;
	typedef unsigned long   GLuint;
	typedef float           GLfloat;
	typedef float           GLclampf;
	typedef double          GLdouble;
	typedef double          GLclampd;
	typedef void            GLvoid;
#else
	typedef unsigned int    GLenum;
	typedef unsigned char   GLboolean;
	typedef unsigned int    GLbitfield;
	typedef void            GLvoid;
	typedef signed char     GLbyte;     /* 1-byte signed */
	typedef short           GLshort;    /* 2-byte signed */
	typedef int             GLint;      /* 4-byte signed */
	typedef unsigned char   GLubyte;    /* 1-byte unsigned */
	typedef unsigned short  GLushort;   /* 2-byte unsigned */
	typedef unsigned int    GLuint;     /* 4-byte unsigned */
	typedef int             GLsizei;    /* 4-byte signed */
	typedef float           GLfloat;    /* single precision float */
	typedef float           GLclampf;   /* single precision float in [0,1] */
	typedef double          GLdouble;   /* double precision float */
	typedef double          GLclampd;   /* double precision float in [0,1] */
#endif

typedef char GLchar;            /* native character */

typedef ptrdiff_t           GLintptr;
typedef ptrdiff_t           GLsizeiptr;
typedef ptrdiff_t           GLintptrARB;
typedef ptrdiff_t           GLsizeiptrARB;
typedef char                GLcharARB;     /* native character */
typedef unsigned int        GLhandleARB;   /* shader object handle */
typedef unsigned short      GLhalfARB;
typedef unsigned short      GLhalfNV;
typedef unsigned short      GLhalf;
typedef int64_t             GLint64EXT;
typedef uint64_t            GLuint64EXT;
typedef int64_t             GLint64;
typedef uint64_t            GLuint64;
typedef struct __GLsync *   GLsync;