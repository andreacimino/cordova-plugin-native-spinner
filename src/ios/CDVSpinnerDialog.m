//
//  CDVSpinnerDialog.m
//
//  Created by Domonkos Pál on 2014.01.27..
//
//

#import "CDVSpinnerDialog.h"

@interface CDVSpinnerDialog () {
    UIActivityIndicatorView *indicator;
    NSString *callbackId;
    NSString *title;
    NSString *message;
    NSString *content;
    NSNumber *isFixed;
    NSString *alpha;
    NSString *red;
    NSString *green;
    NSString *blue;
}

@property (nonatomic, retain) UIActivityIndicatorView *indicator;
@property (nonatomic, retain) UIView *overlay;
@property (nonatomic, retain) UILabel *messageView;


@end

@implementation CDVSpinnerDialog

@synthesize indicator = _indicator;
@synthesize overlay = _overlay;
@synthesize messageView = _messageView;

-(CGRect)rectForView {
    if ((NSFoundationVersionNumber <= 1047.25 /* 7.1 */) && UIInterfaceOrientationIsLandscape([UIApplication sharedApplication].statusBarOrientation)) {
        return CGRectMake( 0.0f, 0.0f, [[UIScreen mainScreen]bounds].size.height, [UIScreen mainScreen].bounds.size.width);
    }
    float screen_width = [[UIScreen mainScreen]bounds].size.width;
    float screen_height = [UIScreen mainScreen].bounds.size.height;

    float width = [[UIScreen mainScreen]bounds].size.width/1.8;
    float height = [UIScreen mainScreen].bounds.size.height/7.5;
    return CGRectMake( (screen_width-width)/2, (screen_height -height)/2, width, height);
}

- (void)handleTapGesture:(UITapGestureRecognizer *)gesture
{
    CDVPluginResult *result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [result setKeepCallbackAsBool:true];
    if (!isFixed.boolValue) {
        [result setKeepCallbackAsBool:false];
        [self hide];
    }
    [self.commandDelegate sendPluginResult:result callbackId:callbackId];
}

- (UIView *)overlay {
    if (!_overlay) {
        _overlay = [[UIView alloc] initWithFrame:self.rectForView];
        _overlay.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.6];
        //_indicator.fr
        _indicator = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
        
        float screen_width = [[UIScreen mainScreen]bounds].size.width;
        float screen_height = [UIScreen mainScreen].bounds.size.height;
        
        float width = [[UIScreen mainScreen]bounds].size.width/1.8;
        float height = [UIScreen mainScreen].bounds.size.height/7.5f;
        int spinner_width = 15;
        _indicator.frame = CGRectMake((width/2) -spinner_width, ((height/2)/2)-8, _indicator.frame.size.width, _indicator.frame.size.height);

    //     _indicator.ce = _overlay.center;
       // indicator.center  = CGPointMake(800, 800);
     //   indicator.frame = self.rectForView;
        [_indicator startAnimating];
        [_overlay addSubview:_indicator];
       CGRect rect = CGRectMake(0, 0, width, (1.5*height)+7);

      _messageView = [[UILabel alloc] initWithFrame: rect];
        [_messageView setText: content];
        [_messageView setTextColor: UIColor.whiteColor];
        [_messageView setBackgroundColor: [UIColor colorWithRed:0 green:0 blue:0 alpha:0]];
        [_messageView setTextAlignment: NSTextAlignmentCenter];
    //    _messageView.center = (CGPoint){_overlay.center.x, _overlay.center.y + 40};
        _messageView.font = [UIFont fontWithName:@"Helvetica" size:(14.0)];
        _messageView.lineBreakMode = UILineBreakModeWordWrap;
        _messageView.numberOfLines = 0;
        [_overlay addSubview:_messageView];
 

        UITapGestureRecognizer *tapRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleTapGesture:)];
        [_overlay addGestureRecognizer: tapRecognizer];
    }
    return _overlay;
}


- (void) show:(CDVInvokedUrlCommand*)command {

    callbackId = command.callbackId;

    title = [command argumentAtIndex:0];
    message = [command argumentAtIndex:1];
    content = [command argumentAtIndex:2];
    isFixed = [command argumentAtIndex:3];
    
    UIViewController *rootViewController = [[[[UIApplication sharedApplication] delegate] window] rootViewController];

    [[self getTopMostViewController].view addSubview:self.overlay];

}

- (void) hide:(CDVInvokedUrlCommand*)command {
    [self hide];
}

- (void) hide {
    if (_overlay) {
        [self.indicator stopAnimating];
        [self.indicator removeFromSuperview];
        [self.messageView removeFromSuperview];
        [self.overlay removeFromSuperview];
        _indicator = nil;
        _messageView = nil;
        _overlay = nil;
    }
}
- (UIViewController*) getTopMostViewController {
    UIViewController *presentingViewController = [[[UIApplication sharedApplication] delegate] window].rootViewController;
    while (presentingViewController.presentedViewController != nil) {
        presentingViewController = presentingViewController.presentedViewController;
    }
    return presentingViewController;
}

#pragma mark - PRIVATE METHODS


@end


