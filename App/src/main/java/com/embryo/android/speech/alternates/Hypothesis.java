package com.embryo.android.speech.alternates;

import com.embryo.speech.common.Alternates;
import com.google.common.base.Objects;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import javax.annotation.Nullable;

public abstract class Hypothesis {
    private final String mText;
    @Nullable
    private ImmutableList<Span> mSpans;

    private Hypothesis(CharSequence paramCharSequence) {
        this.mText = paramCharSequence.toString();
    }

    public static Hypothesis fromAlternateSpanProtos(CharSequence paramCharSequence, @Nullable Iterable<Alternates.AlternateSpan> paramIterable) {
        if (paramIterable == null) {
            return fromText(paramCharSequence);
        }
        return new AlternateSpanProtoBasedHypothesis(paramCharSequence, paramIterable);
    }

    public static Hypothesis fromSpans(CharSequence paramCharSequence, @Nullable Iterable<Span> paramIterable) {
        if (paramIterable == null) {
            return fromText(paramCharSequence);
        }
        return new SpanBasedHypothesis(paramCharSequence, paramIterable);
    }

    public static Hypothesis fromText(CharSequence paramCharSequence) {
        return new TextOnlyHypothesis(paramCharSequence);
    }

    protected abstract ImmutableList<Span> computeSpans();

    public boolean equals(Object paramObject) {
        boolean bool1 = paramObject instanceof Hypothesis;
        boolean bool2 = false;
        if (bool1) {
            Hypothesis localHypothesis = (Hypothesis) paramObject;
            boolean bool3 = Objects.equal(this.mText, localHypothesis.mText);
            bool2 = false;
            if (bool3) {
                boolean bool4 = Objects.equal(getSpans(), localHypothesis.getSpans());
                bool2 = false;
                if (bool4) {
                    bool2 = true;
                }
            }
        }
        return bool2;
    }

    public abstract int getSpanCount();

    public final ImmutableList<Span> getSpans() {
        if (this.mSpans == null) {
            this.mSpans = computeSpans();
        }
        return this.mSpans;
    }

    public final String getText() {
        return this.mText;
    }

    public int hashCode() {
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] = this.mText;
        arrayOfObject[1] = getSpans();
        return Objects.hashCode(arrayOfObject);
    }

    public String toString() {
        return "Hypothesis: [" + this.mText + "] with " + getSpanCount() + " span(s)";
    }

    private static final class AlternateSpanProtoBasedHypothesis
            extends Hypothesis {
        private final ImmutableList<Alternates.AlternateSpan> mAlternateSpans;

        public AlternateSpanProtoBasedHypothesis(CharSequence paramCharSequence, Iterable<Alternates.AlternateSpan> paramIterable) {
            super(null);
            this.mAlternateSpans = ImmutableList.copyOf(paramIterable);
        }

        private int getOffsetUtf16(byte[] utf8Bytes, int offsetUtf8) {
            return offsetUtf8;
        }

        private byte[] getTextAsUtf8() {
            try {
                byte[] arrayOfByte = getText().getBytes("UTF-8");
                return arrayOfByte;
            } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
                throw Throwables.propagate(localUnsupportedEncodingException);
            }
        }

        protected ImmutableList<Hypothesis.Span> computeSpans() {
            ImmutableList.Builder localBuilder1 = ImmutableList.builder();
            Iterator localIterator = this.mAlternateSpans.iterator();
            while (localIterator.hasNext()) {
                Alternates.AlternateSpan localAlternateSpan = (Alternates.AlternateSpan) localIterator.next();
                byte[] arrayOfByte = getTextAsUtf8();
                int i = getOffsetUtf16(arrayOfByte, localAlternateSpan.getStart());
                int j = getOffsetUtf16(arrayOfByte, localAlternateSpan.getStart() + localAlternateSpan.getLength());
                ImmutableList.Builder localBuilder2 = ImmutableList.builder();
                for (int k = 0; k < localAlternateSpan.getAlternatesCount(); k++) {
                    localBuilder2.add(localAlternateSpan.getAlternates(k).getText());
                }
                localBuilder1.add(new Hypothesis.Span(i, j, localAlternateSpan.getStart(), localAlternateSpan.getLength(), localBuilder2.build()));
            }
            return localBuilder1.build();
        }

        public int getSpanCount() {
            return this.mAlternateSpans.size();
        }
    }

    public static class Span {
        public final ImmutableList<String> mAlternates;
        public final int mUtf16End;
        public final int mUtf16Start;
        public final int mUtf8Length;
        public final int mUtf8Start;

        public Span(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ImmutableList<String> paramImmutableList) {
            this.mUtf16Start = paramInt1;
            this.mUtf16End = paramInt2;
            this.mUtf8Start = paramInt3;
            this.mUtf8Length = paramInt4;
            this.mAlternates = paramImmutableList;
        }

        public boolean equals(Object paramObject) {
            boolean bool1 = paramObject instanceof Span;
            boolean bool2 = false;
            if (bool1) {
                Span localSpan = (Span) paramObject;
                int i = this.mUtf16Start;
                int j = localSpan.mUtf16Start;
                bool2 = false;
                if (i == j) {
                    int k = this.mUtf16End;
                    int m = localSpan.mUtf16End;
                    bool2 = false;
                    if (k == m) {
                        int n = this.mUtf8Start;
                        int i1 = localSpan.mUtf8Start;
                        bool2 = false;
                        if (n == i1) {
                            int i2 = this.mUtf8Length;
                            int i3 = localSpan.mUtf8Length;
                            bool2 = false;
                            if (i2 == i3) {
                                boolean bool3 = Objects.equal(this.mAlternates, localSpan.mAlternates);
                                bool2 = false;
                                if (bool3) {
                                    bool2 = true;
                                }
                            }
                        }
                    }
                }
            }
            return bool2;
        }

        public int hashCode() {
            Object[] arrayOfObject = new Object[5];
            arrayOfObject[0] = Integer.valueOf(this.mUtf16Start);
            arrayOfObject[1] = Integer.valueOf(this.mUtf16End);
            arrayOfObject[2] = Integer.valueOf(this.mUtf8Start);
            arrayOfObject[3] = Integer.valueOf(this.mUtf8Length);
            arrayOfObject[4] = this.mAlternates;
            return Objects.hashCode(arrayOfObject);
        }

        public String toString() {
            return Objects.toStringHelper(this).add("mUtf16Start", this.mUtf16Start).add("mUtf16End", this.mUtf16End).add("mUtf8Start", this.mUtf8Start).add("mUtf8Length", this.mUtf8Length).add("mAlternates", this.mAlternates).toString();
        }

        public Span withAlternates(ImmutableList<String> paramImmutableList) {
            return new Span(this.mUtf16Start, this.mUtf16End, this.mUtf8Start, this.mUtf8Length, paramImmutableList);
        }
    }

    private static final class SpanBasedHypothesis
            extends Hypothesis {
        private final ImmutableList<Hypothesis.Span> mSpans;

        public SpanBasedHypothesis(CharSequence paramCharSequence, Iterable<Hypothesis.Span> paramIterable) {
            super(null);
            this.mSpans = ImmutableList.copyOf(paramIterable);
        }

        protected ImmutableList<Hypothesis.Span> computeSpans() {
            return this.mSpans;
        }

        public int getSpanCount() {
            return this.mSpans.size();
        }
    }

    private static final class TextOnlyHypothesis
            extends Hypothesis {
        public TextOnlyHypothesis(CharSequence paramCharSequence) {
            super(null);
        }

        protected ImmutableList<Hypothesis.Span> computeSpans() {
            return ImmutableList.of();
        }

        public int getSpanCount() {
            return 0;
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     Hypothesis

 * JD-Core Version:    0.7.0.1

 */