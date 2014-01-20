#import <Foundation/Foundation.h>
#import <CoreLocation/CoreLocation.h>

#import "ServerManagerDelegate.h"
#import "ServerCommunicatorDelegate.h"

@class ServerCommunicator;

@interface ServerManager : NSObject<ServerCommunicatorDelegate>

@property (strong, nonatomic) ServerCommunicator *communicator;
@property (weak, nonatomic) id<ServerManagerDelegate> delegate;

- (void) fetchMails;
- (void) fetchSimpleSearch:(NSString*) aux;
- (void) fetchComplexSearchWithFrom:(NSString*) from withSubject:(NSString*) subject withBody:(NSString*) body withFromDate:(NSString*) fromdate wihtToDate: (NSString*) toDate;

@end

