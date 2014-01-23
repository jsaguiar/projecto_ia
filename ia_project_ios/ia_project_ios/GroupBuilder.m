//
//  GroupBuilder.m
//  ws_project_ios
//
//  Created by Joao Aguiar on 12/14/13.
//  Copyright (c) 2013 Joao Aguiar. All rights reserved.
//

#import "GroupBuilder.h"
#import "email.h"

@implementation GroupBuilder
+ (NSArray *)receivedSimpleSearchJSON:(NSData *)objectNotation error:(NSError **)error{
    NSLog(@"received agora implementa");
    NSMutableArray *groups = [[NSMutableArray alloc] init];

    return groups;
}

+ (NSArray *)mailsFromJSON:(NSData *)objectNotation error:(NSError **)error
{

    NSError *localError = nil;
    NSDictionary *parsedObject = [NSJSONSerialization JSONObjectWithData:objectNotation options:0 error:&localError];
    
    if (localError != nil) {
        *error = localError;
        return nil;
    }
    
    NSMutableArray *groups = [[NSMutableArray alloc] init];
    
    NSArray *results = [parsedObject valueForKey:@"response"];
    results =[results valueForKey:@"docs"];
    
    for (NSDictionary *groupDic in results) {
        email *group = [[email alloc] init];
        
        NSString *received=[groupDic valueForKey:@"id"];
        [group setValue:received forKey:@"identifier"];
       
        received=[groupDic valueForKey:@"subject"];
        [group setValue:received forKey:@"subject"];
        
         received=[groupDic valueForKey:@"body"];
        [group setValue:received forKey:@"body"];
        
        received=[groupDic valueForKey:@"date"];
        [group setValue:received forKey:@"dateEmail"];
        
        received=[groupDic valueForKey:@"from"];
        [group setValue:received forKey:@"from"];
        
        received=[groupDic valueForKey:@"to"];
        [group setValue:received forKey:@"to"];
        
        received=[groupDic valueForKey:@"polarity"];
        [group setValue:received forKey:@"polarity"];
        
        received=[groupDic valueForKey:@"categories"];
        NSArray *cat=[received componentsSeparatedByString:@";"];
        [group setValue:cat forKey:@"categories"];
        
        
        [groups addObject:group];

        /*NSArray *aux=[received componentsSeparatedByString:@"#"];
        if ([aux count] >= 1 ){
            [group setValue:aux[1] forKey:@"name" ];
            [groups addObject:group];
        }*/
    }

    NSLog(@"Finish");
    return groups;
}
@end
