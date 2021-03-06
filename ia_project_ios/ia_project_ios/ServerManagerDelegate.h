//
//  ServerManagerDelegate.h
//  ws_project_ios
//
//  Created by Joao Aguiar on 12/14/13.
//  Copyright (c) 2013 Joao Aguiar. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol ServerManagerDelegate <NSObject>
- (void)didReceiveMails:(NSArray *)groups;
- (void)didReceiveSimpleSearch: (NSArray *)groups;
- (void)fetchingGroupsFailedWithError:(NSError *)error;

@end
