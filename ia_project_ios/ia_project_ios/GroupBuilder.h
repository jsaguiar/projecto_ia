//
//  GroupBuilder.h
//  ws_project_ios
//
//  Created by Joao Aguiar on 12/14/13.
//  Copyright (c) 2013 Joao Aguiar. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface GroupBuilder : NSObject
+ (NSArray *)mailsFromJSON:(NSData *)objectNotation error:(NSError **)error;
+ (NSArray *)receivedSimpleSearchJSON:(NSData *)objectNotation error:(NSError **)error;


@end
