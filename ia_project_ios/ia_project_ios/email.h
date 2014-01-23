//
//  email.h
//  ia_project:ios
//
//  Created by Joao Aguiar on 12/17/13.
//  Copyright (c) 2013 Joao Aguiar. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface email : NSObject

@property (nonatomic, copy) NSString *identifier;
@property (nonatomic, copy) NSString *subject;
@property (nonatomic, copy) NSString *dateEmail;
@property (nonatomic, copy) NSString *body;
@property (nonatomic, copy) NSString *from;
@property (nonatomic, copy) NSString *to;
@property (nonatomic, copy) NSString *polarity;
@property (nonatomic, copy) NSArray *categories;
@property (nonatomic, copy) NSNumber *score;

@end
