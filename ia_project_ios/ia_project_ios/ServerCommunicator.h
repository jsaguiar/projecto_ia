//
//  ServerCommunicator.h
//  ws_project_ios
//
//  Created by Joao Aguiar on 12/14/13.
//  Copyright (c) 2013 Joao Aguiar. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ServerCommunicatorDelegate.h"
#import <CoreLocation/CoreLocation.h>


@protocol ServerCommunicatorDelegate;

@interface ServerCommunicator : NSObject
@property (weak, nonatomic) id<ServerCommunicatorDelegate> delegate;

- (void)getMails;
-(void) getSimpleSearchWithString:(NSString*) aux;
-(void) getComplexSearchWithFrom:(NSString*) from withSubject:(NSString*) subject withBody:(NSString*) body withFromDate:(NSString*) fromdate wihtToDate: (NSString*) toDate;



@end
