//
//  ServerCommunicatorDelegate.h
//  
//
//  Created by Joao Aguiar on 12/14/13.
//
//

#import <Foundation/Foundation.h>

@protocol ServerCommunicatorDelegate <NSObject>
- (void)receivedMailsJSON:(NSData *)objectNotation;
- (void)receivedSimpleSearchJSON:(NSData *)objectNotation;

- (void)fetchingGroupsFailedWithError:(NSError *)error;

@end
