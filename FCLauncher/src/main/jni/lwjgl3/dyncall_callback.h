/*

 Package: dyncall
 Library: dyncallback
 File: dyncallback/dyncall_callback.h
 Description: Callback - Interface
 License:

   Copyright (c) 2007-2016 Daniel Adler <dadler@uni-goettingen.de>,
                           Tassilo Philipp <tphilipp@potion-studios.com>

   Permission to use, copy, modify, and distribute this software for any
   purpose with or without fee is hereby granted, provided that the above
   copyright notice and this permission notice appear in all copies.

   THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
   WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
   MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
   ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
   WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
   ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
   OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.

*/

#ifndef DYNCALL_CALLBACK_H
#define DYNCALL_CALLBACK_H

#include "dyncall_args.h"
#include "dyncall_signature.h"
#include "dyncall_value.h"

typedef struct DCCallback DCCallback;

// return value is the type encoded as a signature char (character of the set [vBcCsSiIjJlLfd]).
typedef char (DCCallbackHandler)(DCCallback* pcb, DCArgs* args, DCValue* result, void* userdata);

#ifdef __cplusplus
extern "C" {
#endif 

DCCallback* dcbNewCallback(const char* signature, DCCallbackHandler* funcptr, void* userdata);
void        dcbInitCallback(DCCallback* pcb, const char* signature, DCCallbackHandler* handler, void* userdata);
void        dcbFreeCallback(DCCallback* pcb);
void*       dcbGetUserData (DCCallback* pcb);

#ifdef __cplusplus
}
#endif 

#endif /* DYNCALL_CALLBACK_H */
