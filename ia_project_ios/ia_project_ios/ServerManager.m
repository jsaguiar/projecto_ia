#import "ServerManager.h"
#import "GroupBuilder.h"
#import "ServerCommunicator.h"

@implementation ServerManager

- (void)fetchMails
{
    [self.communicator getMails];
}

-(void) fetchSimpleSearch:(NSString*)aux{
    [self.communicator getSimpleSearchWithString:aux];
}

-(void) fetchComplexSearchWithFrom:(NSString*) from withSubject:(NSString*) subject withBody:(NSString*) body withFromDate:
(NSString*) fromdate wihtToDate: (NSString*) toDate{
    [self.communicator getComplexSearchWithFrom:from withSubject:subject withBody:body withFromDate:fromdate wihtToDate:toDate];

}



#pragma mark - MeetupCommunicatorDelegate

- (void)receivedMailsJSON:(NSData *)objectNotation
{
    NSError *error = nil;
    NSArray *groups = [GroupBuilder mailsFromJSON:objectNotation error:&error];
    
    if (error != nil) {
        [self.delegate fetchingGroupsFailedWithError:error];
        
    } else {
        [self.delegate didReceiveMails:groups];
    }
}

- (void)receivedSimpleSearchJSON:(NSData *)objectNotation{
    NSError *error=nil;
    NSArray *groups = [GroupBuilder receivedSimpleSearchJSON:objectNotation error:&error];
    
    if (error != nil) {
        [self.delegate fetchingGroupsFailedWithError:error];
        
    } else {
        [self.delegate didReceiveSimpleSearch:groups];
    }

}

- (void)fetchingGroupsFailedWithError:(NSError *)error
{
    [self.delegate fetchingGroupsFailedWithError:error];
}

@end