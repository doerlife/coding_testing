function [output] = isreactionSB(model,name)
% isreactionSB: checks if "name" is a reaction in the provided model.
% This function works only for SBmodels. The check is of
% course case sensitive
%
% Output Arguments:
% =================
% output: =1 if "name" is a reaction, =0 if not

% Information:
% ============
% Systems Biology Toolbox for MATLAB
% Copyright (C) 2005-2007 Fraunhofer-Chalmers Research Centre
% Author of the toolbox: Henning Schmidt, henning@hschmidt.de
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
% Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

if ~strcmp(class(model),'SBmodel'),
    error('Given model is not an SBmodel.');
end

% get all reactions of the model
allReactions = SBreactions(model);

% check if "name" is a reaction.
output = 0;
for k = 1:length(allReactions),
    if strcmp(strtrim(name),allReactions{k}),
        output = 1;
        break;
    end
end
return