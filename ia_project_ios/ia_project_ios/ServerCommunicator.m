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

#define URL @"http://192.168.1.6:8983/solr/collection1/"


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
    NSString *query=[NSString stringWithFormat:@"select?q=subject:\"%@\" OR body:\"%@\" OR date:\"%@\" OR from:\"%@\"",aux,aux,aux,aux];
    
    
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
