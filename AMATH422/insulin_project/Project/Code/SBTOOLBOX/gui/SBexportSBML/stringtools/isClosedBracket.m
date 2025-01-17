function [isClosedBracket] = isClosedBracket(character)
%isClosedBracket
% checks wether the given character is a closing bracket
%
%
% USAGE:
% ======
%
% boolean = isClosedBracket(character)
%
% character: a letter or sign
%
% boolean: 1 if character is ']', '}' or ')'
%          otherwise 0

% Information:
% ============
% Systems Biology Toolbox for MATLAB
% Copyright (C) 2005-2007 Fraunhofer-Chalmers Research Centre
% Author of the toolbox: Henning Schmidt, henning@hschmidt.de
% Programmed by Gunnar Drews, Student at University of
% Rostock Department of Computerscience, gunnar.drews@uni-rostock.de
% 
% This program is free software; you can redistribute it and/or
% modify it under the terms of the GNU General Public License
% as published by the Free Software Foundation; either version 2
% of the License, or (at your option) any later version.
% 
% This program is distributed in the hope that it will be useful,
% but WITHOUT ANY WARRANTY; without even the implied warranty of
% MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
% GNU General Public License for more details. 
% 
% You should have received a copy of the GNU General Public License
% along with this program; if not, write to the Free Software
% Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
% USA.

    closedBrackets=['}',']',')'];
    if strfind(closedBrackets, character),
        isClosedBracket = 1;
        return;
    else
        isClosedBracket = 0;
    end
return