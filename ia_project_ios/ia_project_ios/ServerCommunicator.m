//
//  ServerCommunicator.m
//  ws_project_ios
//
//  Created by Joao Aguiar on 12/14/13.
//  Copyright (c) 2013 Joao Aguiar. All rights reserved.
//

#import "ServerCommunicator.h"
#import "ServerCommunicatorDelegate.h"
#import "email.h"

#define URL @"http://169.254.238.131:8983/solr/collection1/"


@implementation ServerCommunicator

-(void) getComplexSearchWithFrom:(NSString*) from withSubject:(NSString*) subject withBody:(NSString*) body withFromDate:(NSString*) fromdate wihtToDate: (NSString*) toDate{
    
   // NSString *url=@"select?q=";
    NSLog(@"subject=%@",subject);
    
    if ( [from isEqual:@""] ){
        from=@"*";
    }
    if ([subject isEqual:@""]){
        subject=@"*";
    }
    if ([body isEqual:@""]){
        body=@"*";
    }
    if ([fromdate isEqual:@""]){
        fromdate=@"*";
    }
    if ([toDate isEqual:@""]){
        toDate=@"*";
    }
    
    NSString *query=[NSString stringWithFormat:@"select?q=subject:\"%@\" AND body:\"%@\" AND from:\"%@\" AND date:[\"%@\" TO \"%@\"]",subject,body,from,fromdate,toDate];
    
    
    NSLog(@"GET MAILS QUERY\n%@\n\n",query);
    query = (NSString *)CFBridgingRelease(CFURLCreateStringByAddingPercentEscapes(
                                                                                  NULL,
                                                                                  (__bridge CFStringRef) query,
                                                                                  NULL,
                                                                                  CFSTR("!'();:@&+$,/%#[]\" "),
                                                                                  kCFStringEncodingUTF8));
    
    
    NSString *urlAsString=[NSString stringWithFormat: @"%@%@&sort=date+desc&rows=100000&wt=json",URL,query];
    //urlAsString=[urlAsString stringByAddingPercentEscapesUsingEncoding:NSASCIIStringEncoding];
    
    NSLog(@"\n%@\n", urlAsString);
    
    NSURL *url = [[NSURL alloc] initWithString:urlAsString];
    
    [NSURLConnection sendAsynchronousRequest:[[NSURLRequest alloc] initWithURL:url] queue:[[NSOperationQueue alloc] init] completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
        
        if (error) {
            [self.delegate fetchingGroupsFailedWithError:error];
        } else {
            [self.delegate receivedMailsJSON:data];
        }
    }];
    
}





-(void) getSimpleSearchWithString:(NSString*) aux{
    NSArray *splited= [aux componentsSeparatedByString:@" "];
    
    NSString *formatQuery;
    if(splited.count>0){
        formatQuery=[NSString stringWithFormat:@"all:\"%@\"",splited[0]];
        for (int i=1; i<splited.count; i++) {
            formatQuery=[NSString stringWithFormat:@"%@+all:\"%@\"",formatQuery,splited[i]];
        }
    }
    
    NSLog(@"%@",formatQuery);
    NSString *query=[NSString stringWithFormat:@"select?q=%@",formatQuery];
    NSLog(@"%@",query);
    
    NSLog(@"GET MAILS QUERY\n%@\n\n",query);
    query = (NSString *)CFBridgingRelease(CFURLCreateStringByAddingPercentEscapes(
                                                                                  NULL,
                                                                                  (__bridge CFStringRef) query,
                                                                                  NULL,
                                                                                  CFSTR("!'();:@&+$,/%#[]\" "),
                                                                                  kCFStringEncodingUTF8));
    
    
    NSString *urlAsString=[NSString stringWithFormat: @"%@%@&sort=date+desc&rows=100000&wt=json",URL,query];
    //urlAsString=[urlAsString stringByAddingPercentEscapesUsingEncoding:NSASCIIStringEncoding];
    
    NSLog(@"\n%@\n", urlAsString);
    
    NSURL *url = [[NSURL alloc] initWithString:urlAsString];
    
    [NSURLConnection sendAsynchronousRequest:[[NSURLRequest alloc] initWithURL:url] queue:[[NSOperationQueue alloc] init] completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
        
        if (error) {
            [self.delegate fetchingGroupsFailedWithError:error];
        } else {
            [self.delegate receivedMailsJSON:data];
        }
    }];

}

- (void)getMails
{

    NSString *query=@"select?q=*:*";
    

    NSLog(@"GET MAILS QUERY\n%@\n\n",query);
    query = (NSString *)CFBridgingRelease(CFURLCreateStringByAddingPercentEscapes(
                                                                                        NULL,
                                                                                        (__bridge CFStringRef) query,
                                                                                        NULL,
                                                                                        CFSTR("!'();:@&+$,/%#[]\" "),
                                                                                        kCFStringEncodingUTF8));

    
    NSString *urlAsString=[NSString stringWithFormat: @"%@%@&sort=date+desc&rows=100000&wt=json",URL,query];
    //urlAsString=[urlAsString stringByAddingPercentEscapesUsingEncoding:NSASCIIStringEncoding];

    

    NSURL *url = [[NSURL alloc] initWithString:urlAsString];
    
    [NSURLConnection sendAsynchronousRequest:[[NSURLRequest alloc] initWithURL:url] queue:[[NSOperationQueue alloc] init] completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
        
        if (error) {
            [self.delegate fetchingGroupsFailedWithError:error];
        } else {
            [self.delegate receivedMailsJSON:data];
        }
    }];    
}

@end
