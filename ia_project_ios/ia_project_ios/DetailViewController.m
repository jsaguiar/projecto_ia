//
//  DetailViewController.m
//  ia_project_ios
//
//  Created by Joao Aguiar on 12/17/13.
//  Copyright (c) 2013 Joao Aguiar. All rights reserved.
//

#import "DetailViewController.h"
#import "email.h"
#import "SearchWebView.h"

@interface DetailViewController ()
@property (strong, nonatomic) UIPopoverController *masterPopoverController;
- (void)configureView;
@end

@implementation DetailViewController

#pragma mark - Managing the detail item

- (void)setDetailItem:(id)newDetailItem
{
    if (_detailItem != newDetailItem) {
        _detailItem = newDetailItem;
        
        // Update the view.
        [self configureView];
    }

    if (self.masterPopoverController != nil) {
        [self.masterPopoverController dismissPopoverAnimated:YES];
    }        
}

- (void)configureView
{
    // Update the user interface for the detail item.

    if (self.detailItem) {
        self.fromLabel.text = [self.detailItem from];
        
        self.toLabel.text=[self.detailItem to];
        self.subjectLabel.text=[self.detailItem subject];
        NSString *dateStr = [self.detailItem dateEmail];
        NSDateFormatter *dateFormat = [[NSDateFormatter alloc] init];
        [dateFormat setDateFormat:@"yyyyMMddHHmmss"];
        NSDate *date = [dateFormat dateFromString:dateStr];
        
        [dateFormat setDateFormat:@"yyyy/MM/dd HH:mm:ss"];
        NSString *myFormattedDate = [dateFormat stringFromDate:date];

        
        self.dateLabel.text =myFormattedDate;
        //self.dateLabel.text = [self.detailItem date];
       
        self.detailDescriptionLabel.text = [self.detailItem description];
        [self formatLabelInTextView:self.subjectLabel];
        if ([[self.detailItem body]  rangeOfString:@"<p>"].location != NSNotFound) {
            [self.webview loadHTMLString:[self.detailItem body] baseURL:nil];
            NSLog(@"%d",[self.webview highlightAllOccurencesOfString:@"regards,"]);
            [self.view bringSubviewToFront:self.webview];



        } else{
            self.bodyTextField.text=[self.detailItem body];
            [self.bodyTextField setEditable:FALSE];
            [self formatTextInTextView:self.bodyTextField];
            
            [self.view bringSubviewToFront:self.bodyTextField];

        }
    }
}



- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    [self configureView];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
/*
-(NSString *) stringByStrippingHTML {
    NSRange r;
    NSString *s = [[self copy] autorelease];
    while ((r = [s rangeOfString:@"<[^>]+>" options:NSRegularExpressionSearch]).location != NSNotFound)
        s = [s stringByReplacingCharactersInRange:r withString:@""];
    return s;
}*/

- (void)formatTextInTextView:(UITextView *)textView
{
    //textView.scrollEnabled = NO;
   // NSRange selectedRange = textView.selectedRange;
    NSString *text = textView.text;
    
    // This will give me an attributedString with the base text-style
    NSMutableAttributedString *attributedString;

         attributedString = [[NSMutableAttributedString alloc] initWithString:text];

    NSRange range = NSMakeRange(0,[text length]);
    
    
    [attributedString addAttribute:NSFontAttributeName
                             value:[UIFont systemFontOfSize:16.0]
                             range: range];
    
    
    NSError *error = nil;
    NSString *regular=@"(";
    
    NSArray *split = [self.search componentsSeparatedByString:@" "];
    for (NSString* word in split) {
        if ([regular compare:@"("]==0) {
            regular=[NSString stringWithFormat:@"%@%@",regular,word];
        }
        regular=[NSString stringWithFormat:@"%@|%@",regular,word];
    }
    regular=[NSString stringWithFormat:@"%@)",regular];
    
    

    
    NSRegularExpression *regex = [NSRegularExpression regularExpressionWithPattern:regular options:NSRegularExpressionCaseInsensitive error:&error];
    NSArray *matches = [regex matchesInString:text
                                      options:0
                                        range:NSMakeRange(0, text.length)];
    
    for (NSTextCheckingResult *match in matches)
    {
        NSRange matchRange = [match rangeAtIndex:0];
        [attributedString addAttribute:NSBackgroundColorAttributeName
                                 value:[UIColor yellowColor]
                                 range:matchRange];
    }
    
    textView.attributedText = attributedString;
    //textView.selectedRange = selectedRange;
    //textView.scrollEnabled = YES;
}



- (void)formatLabelInTextView:(UILabel *)label
{
    NSString *text = label.text;
    
    // This will give me an attributedString with the base text-style
    NSMutableAttributedString *attributedString = [[NSMutableAttributedString alloc] initWithString:text];
    
    NSRange range = NSMakeRange(0,[text length]);
    
    
    [attributedString addAttribute:NSFontAttributeName
                             value:[UIFont systemFontOfSize:16.0]
                             range: range];
    
    
    NSError *error = nil;
    NSString *regular=@"(";
    
    NSArray *split = [self.search componentsSeparatedByString:@" "];
    for (NSString* word in split) {
        if ([regular compare:@"("]==0) {
            regular=[NSString stringWithFormat:@"%@%@",regular,word];
        }
        regular=[NSString stringWithFormat:@"%@|%@",regular,word];
    }
    regular=[NSString stringWithFormat:@"%@)",regular];
    
    
    NSRegularExpression *regex = [NSRegularExpression regularExpressionWithPattern:regular options:0 error:&error];
    NSArray *matches = [regex matchesInString:text
                                      options:0
                                        range:NSMakeRange(0, text.length)];
    
    for (NSTextCheckingResult *match in matches)
    {
        NSRange matchRange = [match rangeAtIndex:0];
        [attributedString addAttribute:NSBackgroundColorAttributeName
                                 value:[UIColor yellowColor]
                                 range:matchRange];
    }
    
    label.attributedText = attributedString;
}



#pragma mark - Split view

- (void)splitViewController:(UISplitViewController *)splitController willHideViewController:(UIViewController *)viewController withBarButtonItem:(UIBarButtonItem *)barButtonItem forPopoverController:(UIPopoverController *)popoverController
{
    barButtonItem.title = NSLocalizedString(@"Master", @"Master");
    [self.navigationItem setLeftBarButtonItem:barButtonItem animated:YES];
    self.masterPopoverController = popoverController;
}

- (void)splitViewController:(UISplitViewController *)splitController willShowViewController:(UIViewController *)viewController invalidatingBarButtonItem:(UIBarButtonItem *)barButtonItem
{
    // Called when the view is shown again in the split view, invalidating the button and popover controller.
    [self.navigationItem setLeftBarButtonItem:nil animated:YES];
    self.masterPopoverController = nil;
}

@end
