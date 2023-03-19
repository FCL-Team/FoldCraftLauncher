/*!A dlopen library that bypasses mobile system limitation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Copyright (C) 2020-present, TBOOX Open Source Group.
 *
 * @author      ruki
 * @file        byopen.h
 *
 */
#ifndef BY_BYOPEN_H
#define BY_BYOPEN_H

#ifdef __cplusplus
extern "C" {
#endif

/* //////////////////////////////////////////////////////////////////////////////////////
 * includes
 */
#include "prefix.h"

/* //////////////////////////////////////////////////////////////////////////////////////
 * types
 */

/// the dlopen flag enum
typedef enum __by_dlopen_flag_e
{
    BY_RTLD_LAZY    = 1
,   BY_RTLD_NOW     = 2

}by_dlopen_flag_e;

/* //////////////////////////////////////////////////////////////////////////////////////
 * interfaces
 */

/*! The function dlopen() loads the dynamic library and returns an opaque "handle".
 *
 * @param filename  the dynamic library file named by the null-terminated string filename 
 * @param flag      the load flag
 *
 * @return          the dynamic library handle
 */
by_pointer_t        by_dlopen(by_char_t const* filename, by_int_t flag);

/*! get the address where that symbol is loaded into memory
 *
 * @param handle    the dynamic library handle
 *
 * @return          the symbol address
 */
by_pointer_t        by_dlsym(by_pointer_t handle, by_char_t const* symbol);

/*! It decrements the reference count on the dynamic library handle handle. 
 * If the reference count drops to zero and no other loaded libraries use symbols in it, then the dynamic library is unloaded. 
 *
 * @param handle    the dynamic library handle
 *
 * @return          0 on success, and nonzero on error
 */
by_int_t            by_dlclose(by_pointer_t handle);

#ifdef __cplusplus
}
#endif
#endif
