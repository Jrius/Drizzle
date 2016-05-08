/*
    Drizzle - A general Myst tool.
    Copyright (C) 2008  Dustin Bernard.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/ 

package prpobjects;

import shared.Flt;
import uru.context; import shared.readexception;
import uru.Bytestream;
import uru.Bytedeque;
import shared.e;
import shared.m;
import shared.b;
//import java.util.Vector;

/**
 *
 * @author user
 */
public class x0006Layer extends uruobj
{
    public static final int kBlendTest = 0x1,
                            kBlendAlpha = 0x2,
                            kBlendMult = 0x4,
                            kBlendAdd = 0x8,
                            kBlendAddColorTimesAlpha = 0x10,
                            kBlendAntiAlias = 0x20,
                            kBlendDetail = 0x40,
                            kBlendNoColor = 0x80,
                            kBlendMADD = 0x100,
                            kBlendDot3 = 0x200,
                            kBlendAddSigned = 0x400,
                            kBlendAddSigned2X = 0x800,
                            kBlendMask = 0xF5E,
                            kBlendInvertAlpha = 0x1000,
                            kBlendInvertColor = 0x2000,
                            kBlendAlphaMult = 0x4000,
                            kBlendAlphaAdd = 0x8000,
                            kBlendNoVtxAlpha = 0x10000,
                            kBlendNoTexColor = 0x20000,
                            kBlendNoTexAlpha = 0x40000,
                            kBlendInvertVtxAlpha = 0x80000,
                            kBlendAlphaAlways = 0x100000,
                            kBlendInvertFinalColor = 0x200000,
                            kBlendInvertFinalAlpha = 0x400000,
                            kBlendEnvBumpNext = 0x800000,
                            kBlendSubtract = 0x1000000,
                            kBlendRevSubtract = 0x2000000,
                            kBlendAlphaTestHigh = 0x4000000;
    
    public static final int kClampTextureU = 0x1,
                            kClampTextureV = 0x2,
                            kClampTexture = 0x3;
    
    public static final int kShadeSoftShadow = 0x1,
                            kShadeNoProjectors = 0x2,
                            kShadeEnvironMap = 0x4,
                            kShadeVertexShade = 0x20,
                            kShadeNoShade = 0x40,
                            kShadeBlack = 0x40,
                            kShadeSpecular = 0x80,
                            kShadeNoFog = 0x100,
                            kShadeWhite = 0x200,
                            kShadeSpecularAlpha = 0x400,
                            kShadeSpecularColor = 0x800,
                            kShadeSpecularHighlight = 0x1000,
                            kShadeVertColShade = 0x2000,
                            kShadeInherit = 0x4000,
                            kShadeIgnoreVtxIllum = 0x8000,
                            kShadeEmissive = 0x10000,
                            kShadeReallyNoFog = 0x20000;
    
    public static final int kZIncLayer = 0x1,
                            kZClearZ = 0x4,
                            kZNoZRead = 0x8,
                            kZNoZWrite = 0x10,
                            kZMask = 0x1C,
                            kZLODBias = 0x20;
    
    public static final int kMiscWireFrame = 0x1,
                            kMiscDrawMeshOutlines = 0x2,
                            kMiscTwoSided = 0x4,
                            kMiscDrawAsSplats = 0x8,
                            kMiscAdjustPlane = 0x10,
                            kMiscAdjustCylinder = 0x20,
                            kMiscAdjustSphere = 0x40,
                            kMiscAdjust = 0x70,
                            kMiscTroubledLoner = 0x80,
                            kMiscBindSkip = 0x100,
                            kMiscBindMask = 0x200,
                            kMiscBindNext = 0x400,
                            kMiscLightMap = 0x800,
                            kMiscUseReflectionXform = 0x1000,
                            kMiscPerspProjection = 0x2000,
                            kMiscOrthoProjection = 0x4000,
                            kMiscProjection = 0x6000,
                            kMiscRestartPassHere = 0x8000,
                            kMiscBumpLayer = 0x10000,
                            kMiscBumpDu = 0x20000,
                            kMiscBumpDv = 0x40000,
                            kMiscBumpDw = 0x80000,
                            kMiscBumpChans = 0xE0000,
                            kMiscNoShadowAlpha = 0x100000,
                            kMiscUseRefractionXform = 0x200000,
                            kMiscCam2Screen = 0x400000,
                            kAllMiscFlags = 0xFF;
    
    //Objheader xheader;
    public x0041LayerInterface parent;
    public int flags1; // blend
    public int flags2; // clamp
    public int flags3; // shade
    public int flags4; // z
    public int flags5; // misc
    public Transmatrix matrix;
    public Rgba ambient; //preshade //was ambient
    public Rgba diffuse; //runtime //was diffuse
    public Rgba emissive; //ambient //was emissive
    public Rgba specular; //specular //was specular
    public int uvwSource;
    public Flt opacity; //float 0 to 1
    public Flt lodbias;
    public Flt specularPower; //a float with integer! values.
    public Uruobjectref texture;
    public Uruobjectref shader1;
    public Uruobjectref shader2;
    public Transmatrix identity;
    
    public x0006Layer(context c) throws readexception //,boolean hasHeader)
    {
        shared.IBytestream data = c.in;
        //if(hasHeader) xheader = new Objheader(c);
        parent = new x0041LayerInterface(c); //contains the plmipmap.
        //the next 5 flags are the hsGMatState
        flags1 = data.readInt(); //blend flags
        flags2 = data.readInt(); //clamp flags
        flags3 = data.readInt(); //shade flags
        flags4 = data.readInt(); //z flags
        flags5 = data.readInt(); //misc flags //there's a wireframe option here(0x1)!
        matrix = new Transmatrix(c); //transform
        ambient = new Rgba(c); //preshade
        diffuse = new Rgba(c); //runtime
        emissive = new Rgba(c); //ambient
        specular = new Rgba(c); //specular
        uvwSource = data.readInt(); //uvwsource
        opacity = new Flt(c); //opacity //float
        lodbias = new Flt(c); //LOD bias //float
        specularPower = new Flt(c); //Specular power //float
        texture = new Uruobjectref(c); //texture
        shader1 = new Uruobjectref(c); //vertex shader
        shader2 = new Uruobjectref(c); //pixel shader
        identity = new Transmatrix(c); //bumpEnvXfm
        
        
    }
    public void compile(Bytedeque data)
    {
        parent.compile(data);
        data.writeInt(flags1);
        data.writeInt(flags2);
        data.writeInt(flags3);
        data.writeInt(flags4);
        data.writeInt(flags5);
        matrix.compile(data);
        ambient.compile(data);
        diffuse.compile(data);
        emissive.compile(data);
        specular.compile(data);
        data.writeInt(uvwSource);
        opacity.compile(data);
        lodbias.compile(data);
        specularPower.compile(data);
        texture.compile(data);
        shader1.compile(data);
        shader2.compile(data);
        identity.compile(data);
        
    }
    private x0006Layer(){}
    public x0006Layer deepClone()
    {
        x0006Layer result =new x0006Layer();
        result.ambient = ambient.deepClone();
        result.diffuse = diffuse.deepClone();
        result.emissive = emissive.deepClone();
        result.flags1 = flags1;
        result.flags2 = flags2;
        result.flags3 = flags3;
        result.flags4 = flags4;
        result.flags5 = flags5;
        result.identity = identity.deepClone();
        result.lodbias = lodbias.deepClone();
        result.matrix = matrix.deepClone();
        result.opacity = opacity.deepClone();
        result.parent = parent.deepClone();
        result.shader1 = shader1.deepClone();
        result.shader2 = shader2.deepClone();
        result.specular = specular.deepClone();
        result.specularPower = specularPower.deepClone();
        result.texture = texture.deepClone();
        result.uvwSource = uvwSource;
        return result;
    }
    public static x0006Layer createEmpty()
    {
        x0006Layer r = new x0006Layer();
        r.parent = prpobjects.x0041LayerInterface.createEmpty();
        return r;
    }
}
