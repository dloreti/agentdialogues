
% BACKGROUND KNOWLEDGE
 acc(Z) :- arg(X,Y,Z), acc(X), acc(Y). % conclusion of argument is accessible if premise and reasoning are accessible
 acc(Y) :- def(X,Y,Z), acc(X), acc(Z). % a defeater is accessible if premise and conclusion are both accessible
 acc(X) :- bel(X). % accessible if believed to be true
 acc(X) :- und(X). % accessible if understood

 con(Z,W) :- con(W,Z). % conflict is symmetric
 :- con(Z,Z). % conflict is irreflexive

 der(Z) :- arg(X,Y,Z), bel(X), app(Y), not -der(Z). % conclusion Z can be derived by default if believe premise and reasoning applicable
-der(Z) :- der(W), con(W,Z). % conclusion cannot be derived if a conflicting conclusion is derived

 app(Y) :- arg(X,Y,Z), bel(X), acc(Y), not -app(Y). % accessible reasoning is applicable by default if premise believed
 app(Y) :- def(X,Y,Z), app(X), acc(Y), not -app(Y). % accessible defeater is applicable by default if premise of defeater is applicable
-app(Y) :- -app(Y,_). % not applicable if not applicable for any reason 
-app(Y,because(U)) :- arg(X,Y,Z), arg(XX,YY,ZZ), bel(XX), app(YY), def(YY,U,Y), app(U). 
% a possible reason that makes a reasoning Y not applicable is that Y is defeated (undercut) 
% by an applicable argument U whose premise YY is another argument that is applicable
%% (if this explanation is not clear, don't worry - ask us, it's not obvious)
  
 bel(X) :- arg(X,Y,Z), acc(X), not -bel(X). % by default, believe premises 
 bel(X) :- der(X), not -bel(X). % by default, believe what you derive
 bel(X) :- und(X), not -bel(X). % by default, believe what you understand (from communication with another agent)
%-bel(X) :- bel(Y), con(X,Y). % don't believe X and something in conflict with X 
-bel(X) :- der(Y), con(X,Y). % don't believe X if derived something in conflict with X 
-bel(X) :- rebut(X,because(_)). % if X is rebutte, don't believe X
-bel(X) :- undercut(X,because(_)). % if X is undercut, don't believe X

agree(Z)               :- und(Z), bel(Z). % agree with what you understand from communication and believe to be true
rebut(Z,because(YY))   :- und(Z), arg(XX,YY,ZZ), bel(XX), app(YY), der(ZZ), con(Z,ZZ). 
% produce a rebuttal against Z if you have understood Z from communicatin and you can derive a conclusion ZZ that conflicts with Z
undercut(Y,because(U)) :- und(Y), arg(_,Y,_), -app(Y,because(U)). 
% produce an undercutting argument U against a reasoning Y that you have understood from communication, 
% if U is a possible reason that makes Y not applicable

